package org.nguyen.orderjava.controllers;

import java.util.Map;

import org.nguyen.orderjava.exceptions.OrderNotFoundException;
import org.nguyen.orderjava.models.dto.OrderDto;
import org.nguyen.orderjava.models.dto.OrderUpdateDto;
import org.nguyen.orderjava.services.OrderService;
import org.nguyen.orderjava.utils.RestResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/v1/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get order by ID", notes = "Get order details for the provided order ID")
    public OrderDto getOrderById(@RequestParam String id) throws OrderNotFoundException {
        return orderService.getOrderById(id);
    }

    @PostMapping
    @ApiOperation(value = "Create order", notes = "Create an order with the provided order details")
    public Map<String, String> createOrder(@RequestBody OrderDto data) {
        return RestResponseUtils.getResponseJson("id", orderService.saveOrder(data));
    }

    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update order by ID", notes = "Finds an existing order by ID and updates it with the provided update details")
    public Map<String, String> updateOrder(
        @PathVariable String id,
        @RequestBody OrderUpdateDto update
    ) throws OrderNotFoundException {
        return RestResponseUtils.getResponseJson("id", updateOrderById(id, update));
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update order", notes = "Finds an existing order and updates it with the provided update details")
    public Map<String, String> updateOrder(
        @RequestBody OrderUpdateDto update
    ) throws OrderNotFoundException {
            return RestResponseUtils.getResponseJson("id", updateOrderById(update.getId(), update));
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete order", notes = "Finds an existing order by ID and deletes it")
    public void deleteOrder(@PathVariable String id) throws OrderNotFoundException {
        orderService.deleteOrder(id);
    }

    private String updateOrderById(String id, OrderUpdateDto update) throws OrderNotFoundException {
        return orderService.updateOrder(id, update);
    }
}
