package ru.otus.processor;

import ru.otus.model.Message;

public class ProcessorException implements Processor {
    private final DateTimeProvider seconds;
    public ProcessorException(DateTimeProvider seconds) {

        this.seconds = seconds;
    }
    @Override
    public Message process(Message message) {
        if (seconds.isEvenSecond()) throw new ExceptionSecond();
        return message;
    }
}
