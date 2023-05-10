package ru.otus.processor;

import static java.time.LocalTime.now;

public interface DateTimeProvider {
    default boolean isEvenSecond() {

        return now().getSecond() % 2 == 0;
    }
}
