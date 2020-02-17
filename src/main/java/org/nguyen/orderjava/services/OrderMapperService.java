package org.nguyen.orderjava.services;

import java.util.List;

import org.nguyen.orderjava.models.OrderData;
import org.nguyen.orderjava.models.jpa.InventoryEntry;
import org.nguyen.orderjava.models.jpa.OrderEntry;

public interface OrderMapperService {

    OrderData mapToOrderData(OrderEntry entry, List<InventoryEntry> beanEntry);
}