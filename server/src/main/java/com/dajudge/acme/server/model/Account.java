package com.dajudge.acme.server.model;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private String id;
    private List<String> contact;
    private List<Order> orders = new ArrayList<Order>();

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public List<String> getContact() {
        return contact;
    }

    public void setContact(final List<String> contact) {
        this.contact = contact;
    }

    public List<Order> getOrders() {
        return orders;
    }
}
