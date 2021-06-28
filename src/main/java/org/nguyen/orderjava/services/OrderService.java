package org.nguyen.orderjava.services;

import java.util.List;

import org.nguyen.orderjava.exceptions.OrderNotFoundException;
import org.nguyen.orderjava.models.dto.OrderDto;
import org.nguyen.orderjava.models.dto.OrderUpdateDto;
import org.nguyen.orderjava.models.jpa.InventoryEntryJpa;
import org.nguyen.orderjava.models.jpa.OrderJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepoService orderRepoService;

    private final InventoryRepoService inventoryRepoService;

    @Autowired
    OrderService(OrderRepoService orderRepoService, InventoryRepoService inventoryRepoService) {
        this.orderRepoService = orderRepoService;
        this.inventoryRepoService = inventoryRepoService;
    }

    @Transactional
    public OrderDto getOrderById(String id) throws OrderNotFoundException {
        OrderJpa orderEntry = orderRepoService.findOrderById(id);

        if (orderEntry != null) {
            List<InventoryEntryJpa> beanData = inventoryRepoService.findAllEntries();

            return OrderMapper.mapOrderEntryToOrderData(orderEntry, beanData);
        }
        else {
            throw new OrderNotFoundException(id);
        }
    }

    @Transactional
    public OrderDto updateOrder(String id, OrderUpdateDto update) throws OrderNotFoundException {
        OrderJpa orderEntry = orderRepoService.findOrderById(id);

        if (orderEntry != null) {
            OrderJpa updatedOrder = OrderMapper.updateOrderEntry(orderEntry, update);
            List<InventoryEntryJpa> beanData = inventoryRepoService.findAllEntries();

            orderRepoService.saveOrder(updatedOrder);

            return OrderMapper.mapOrderEntryToOrderData(orderEntry, beanData);
        }
        else {
            throw new OrderNotFoundException(id);
        }
    }

    @Transactional
    public void deleteOrder(String id) throws OrderNotFoundException {
        try {
            orderRepoService.deleteOrderById(id);
        }
        catch (EmptyResultDataAccessException ex) {
            throw new OrderNotFoundException(id);
        }
    }

    @Transactional
    public OrderDto saveOrder(OrderDto orderData) {
        OrderJpa orderEntry = OrderMapper.mapOrderDataToOrderEntry(orderData);
        List<InventoryEntryJpa> beanData = inventoryRepoService.findAllEntries();

        orderRepoService.saveOrder(orderEntry);

        return OrderMapper.mapOrderEntryToOrderData(orderEntry, beanData);
    }
}
