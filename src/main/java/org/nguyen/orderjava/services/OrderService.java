package org.nguyen.orderjava.services;

import org.nguyen.orderjava.models.OrderData;

public interface OrderService {

    OrderData getOrderById(String id);
}
