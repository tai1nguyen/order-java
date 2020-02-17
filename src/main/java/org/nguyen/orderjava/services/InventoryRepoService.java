package org.nguyen.orderjava.services;

import java.util.List;

import org.nguyen.orderjava.models.Bean.BeanType;
import org.nguyen.orderjava.models.jpa.InventoryEntry;

public interface InventoryRepoService {

    InventoryEntry getBeanByType(BeanType beanType);

    List<InventoryEntry> getAllBeans();
}
