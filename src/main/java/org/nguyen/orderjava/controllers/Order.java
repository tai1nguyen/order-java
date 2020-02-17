package org.nguyen.orderjava.controllers;

import org.nguyen.orderjava.models.jpa.OrderEntry;
import org.nguyen.orderjava.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class Order {

    private final OrderService orderService;

    @Autowired
    Order(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public OrderEntry getOrderById(@RequestParam String id) {
        return orderService.getOrderById(id);
    }
}
