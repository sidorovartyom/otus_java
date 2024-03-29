package ru.otus.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.util.HtmlUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.domain.Message;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    private static final String ROOM_VIEW = "1408";
    private static final String TOPIC_TEMPLATE = "/topic/response.";
    private static final String TOPIC_SEE_ALL = TOPIC_TEMPLATE + ROOM_VIEW;

    private final WebClient datastoreClient;
    private final SimpMessagingTemplate template;

    public MessageController(WebClient datastoreClient, SimpMessagingTemplate template) {
        this.datastoreClient = datastoreClient;
        this.template = template;
    }

    private static Flux<Message> processResponse(ClientResponse response) {
        if (response.statusCode().equals(HttpStatus.OK)) {
            return response.bodyToFlux(Message.class);
        } else {
            return response.createException().flatMapMany(Mono::error);
        }
    }

    @MessageMapping("/message.{roomId}")
    @SendTo({TOPIC_TEMPLATE + "{roomId}", TOPIC_SEE_ALL})
    public Message getMessage(@DestinationVariable String roomId, Message message) {
        logger.info("got message:{}, roomId:{}", message, roomId);
        if (ROOM_VIEW.equals(roomId)) {
            logger.info("cannot send message from roomId:{}", roomId);
            return null;
        } else {
            saveMessage(roomId, message).subscribe(msgId -> logger.info("message send id:{}", msgId));
            return new Message(HtmlUtils.htmlEscape(message.messageStr()));
        }
    }


    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        var genericMessage = (GenericMessage<byte[]>) event.getMessage();
        var simpDestination = (String) genericMessage.getHeaders().get("simpDestination");
        if (simpDestination == null) {
            logger.error("Can not get simpDestination header, headers:{}", genericMessage.getHeaders());
            throw new ChatException("Can not get simpDestination header");
        }
        var roomId = parseRoomId(simpDestination);

        Flux<Message> messageFlux = Long.parseLong(ROOM_VIEW) == roomId ? getAllMessages() : getMessagesByRoomId(roomId);
        messageFlux.doOnError(ex -> logger.error("getting messages for roomId:{} failed", roomId, ex))
                .subscribe(message -> template.convertAndSend(simpDestination, message));
    }

    private long parseRoomId(String simpDestination) {
        try {
            return Long.parseLong(simpDestination.replace(TOPIC_TEMPLATE, ""));
        } catch (Exception ex) {
            logger.error("Can not get roomId", ex);
            throw new ChatException("Can not get roomId");
        }
    }

    private Mono<Long> saveMessage(String roomId, Message message) {
        return datastoreClient.post().uri(String.format("/msg/%s", roomId))
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(message)
                .exchangeToMono(response -> response.bodyToMono(Long.class));
    }

    private Flux<Message> getMessagesByRoomId(long roomId) {
        return datastoreClient.get().uri(String.format("/msg/%s", roomId))
                .accept(MediaType.APPLICATION_NDJSON)
                .exchangeToFlux(MessageController::processResponse);
    }

    private Flux<Message> getAllMessages() {
        return datastoreClient.get().uri("/msg")
                .accept(MediaType.APPLICATION_NDJSON)
                .exchangeToFlux(MessageController::processResponse);
    }
}
