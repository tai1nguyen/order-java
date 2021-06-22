package org.nguyen.orderjava.services;

import static org.nguyen.orderjava.literals.Services.ORDER_SERVICE;

import java.util.List;

import org.nguyen.orderjava.exceptions.OrderNotFoundException;
import org.nguyen.orderjava.models.dto.OrderDto;
import org.nguyen.orderjava.models.dto.OrderUpdateDto;
import org.nguyen.orderjava.models.jpa.InventoryEntryJpa;
import org.nguyen.orderjava.models.jpa.OrderEntryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(ORDER_SERVICE)
public class OrderService {

    private final OrderRepoService orderRepoService;
    private final InventoryRepoService inventoryRepoService;
    private final OrderMapperService orderMapperService;

    @Autowired
    OrderService(
            OrderRepoService orderRepoService,
            InventoryRepoService inventoryRepoService,
            OrderMapperService orderMapperService
    ) {
        this.orderRepoService = orderRepoService;
        this.inventoryRepoService = inventoryRepoService;
        this.orderMapperService = orderMapperService;
    }

    @Transactional
    public OrderDto getOrderById(String id) throws OrderNotFoundException {
        OrderEntryJpa orderEntry = orderRepoService.findOrderById(id);

        if (orderEntry != null) {
            List<InventoryEntryJpa> beanData = inventoryRepoService.findAllEntries();

            return orderMapperService.mapOrderEntryToOrderData(id, orderEntry, beanData);
        }
        else {
            throw new OrderNotFoundException(id);
        }
    }

    @Transactional
    public String updateOrder(String id, OrderUpdateDto update) throws OrderNotFoundException {
        OrderEntryJpa orderEntry = orderRepoService.findOrderById(id);

        if (orderEntry != null) {
            OrderEntryJpa updatedOrder = orderMapperService.updateOrderEntry(orderEntry, update);

            return orderRepoService.saveOrder(updatedOrder).getId();
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
    public String saveOrder(OrderDto orderData) {
        OrderEntryJpa orderEntry = orderMapperService.mapOrderDataToOrderEntry(orderData);

        return orderRepoService.saveOrder(orderEntry).getId();
    }
}
