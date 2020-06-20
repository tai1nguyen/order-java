package org.nguyen.orderjava.controllers;

import java.util.HashMap;
import java.util.Map;

import org.nguyen.orderjava.exceptions.OrderNotFoundException;
import org.nguyen.orderjava.models.OrderData;
import org.nguyen.orderjava.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public OrderData getOrderById(@RequestParam String id) throws OrderNotFoundException {
        return orderService.getOrderById(id);
    }

    @PostMapping
    public Map<String, String> createOrder(@RequestBody OrderData data) {
        Map<String, String> response = new HashMap<>();
        String orderId = orderService.saveOrder(data);

        response.put("id", orderId);

        return response;
    }
}
