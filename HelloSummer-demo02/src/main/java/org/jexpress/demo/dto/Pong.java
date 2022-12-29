package org.jexpress.demo.dto;

import java.time.OffsetDateTime;

public class Pong {

    private final String name;
    private final long value;
    private final OffsetDateTime receivedTime;

    public Pong(String name, long value) {
        this.name = name;
        this.value = value;
        this.receivedTime = OffsetDateTime.now();
    }

    public String getName() {
        return name;
    }

    public long getValue() {
        return value;
    }

    public OffsetDateTime getReceivedTime() {
        return receivedTime;
    }

}
