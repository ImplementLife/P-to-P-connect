package com.implementLife.commonDTO.comServerEntity;

import java.util.function.Consumer;

public interface Event {
    EventType getType();
    void execute(Consumer<EventType> consumer);
}
