package org.nguyen.orderjava.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class OrderNotFoundException extends ResponseStatusException {

    private static final long serialVersionUID = 2577856845359637170L;

    public OrderNotFoundException(String orderId) {
        super(HttpStatus.NOT_FOUND, "Order '" + orderId + "' Not Found");
    }
}
