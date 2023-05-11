package ru.otus.listener.homework;


import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.DateTimeProvider;
import ru.otus.processor.ExceptionSecond;
import ru.otus.processor.ProcessorException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExceptionSecondTest {

    @Test
    void secondTest() {
        //given
        var message = new Message.Builder(100L)
                .field11("field11")
                .field12("field12")
                .build();
        var evenSecond = new ProcessorException(new DateTimeProvider() {
            @Override
            public boolean isEvenSecond() {
                return true;
            }
        });

        var oddSecond = new ProcessorException(new DateTimeProvider() {
            @Override
            public boolean isEvenSecond() {
                return false;
            }
        });

        assertThrows(ExceptionSecond.class,
                () -> evenSecond.process(message));
        assertDoesNotThrow(() -> oddSecond.process(message));
    }
}