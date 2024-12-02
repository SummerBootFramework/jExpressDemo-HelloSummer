package org.jexpress.demo.app;

import java.time.OffsetDateTime;

public class MyResponse {

    private final String type;
    private String value;
    private final OffsetDateTime ts;

    public MyResponse(String type, String value) {
        this.type = type;
        this.value = value;
        ts = OffsetDateTime.now();
    }

    public String getType() {
        return type;
    }

    public OffsetDateTime getTs() {
        return ts;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
