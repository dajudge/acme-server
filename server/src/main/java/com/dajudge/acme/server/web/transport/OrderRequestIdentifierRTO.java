package com.dajudge.acme.server.web.transport;


import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class OrderRequestIdentifierRTO {
    private String type;
    private String value;

    public OrderRequestIdentifierRTO() {
    }

    public OrderRequestIdentifierRTO(final String type, final String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
