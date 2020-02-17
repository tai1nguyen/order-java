package org.nguyen.orderjava.services;

import org.nguyen.orderjava.models.jpa.OrderEntry;

public interface OrderRepoService {

    OrderEntry getOrderById(String id);

    void deleteOrderById(String id);

    void updateOrderById(String id);
}
