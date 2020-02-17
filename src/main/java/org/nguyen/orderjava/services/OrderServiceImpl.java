package org.nguyen.orderjava.services;

import java.util.List;

import org.nguyen.orderjava.models.OrderData;
import org.nguyen.orderjava.models.jpa.InventoryEntry;
import org.nguyen.orderjava.models.jpa.OrderEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepoService orderRepoService;
    private final InventoryRepoService inventoryRepoService;
    private final OrderMapperService orderMapperService;

    @Autowired
    OrderServiceImpl(
        OrderRepoService orderRepoService,
        InventoryRepoService inventoryRepoService,
        OrderMapperService orderMapperService
    ) {
        this.orderRepoService = orderRepoService;
        this.inventoryRepoService = inventoryRepoService;
        this.orderMapperService = orderMapperService;
    }

    @Override
    public OrderData getOrderById(String id) {
        OrderEntry orderEntry = orderRepoService.getOrderById(id);
        List<InventoryEntry> beanData = inventoryRepoService.getAllBeans();

        return orderMapperService.mapToOrderData(orderEntry, beanData);
    }
}
