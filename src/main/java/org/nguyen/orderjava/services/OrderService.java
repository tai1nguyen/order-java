package org.nguyen.orderjava.services;

import static org.nguyen.orderjava.literals.Services.ORDER_SERVICE;

import java.util.List;

import org.nguyen.orderjava.models.OrderData;
import org.nguyen.orderjava.models.jpa.InventoryEntry;
import org.nguyen.orderjava.models.jpa.OrderEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public OrderData getOrderById(String id) {
        OrderEntry orderEntry = orderRepoService.findOrderById(id);
        List<InventoryEntry> beanData = inventoryRepoService.findAllEntries();

        return orderMapperService.mapToOrderData(orderEntry, beanData);
    }
}
