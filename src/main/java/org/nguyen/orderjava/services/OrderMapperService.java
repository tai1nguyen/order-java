package org.nguyen.orderjava.services;

import static org.nguyen.orderjava.literals.Services.ORDER_MAPPER_SERVICE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.nguyen.orderjava.models.Bean;
import org.nguyen.orderjava.models.Bean.BeanType;
import org.nguyen.orderjava.models.OrderData;
import org.nguyen.orderjava.models.jpa.InventoryEntry;
import org.nguyen.orderjava.models.jpa.OrderContentEntry;
import org.nguyen.orderjava.models.jpa.OrderEntry;
import org.springframework.stereotype.Service;

@Service(ORDER_MAPPER_SERVICE)
public class OrderMapperService {

    /**
     * Maps an order entry from the database into an object
     * that can be consumed by the client.
     * @param id
     * @param orderEntry
     * @param inventoryEntries
     * @return OrderData
     */
    public OrderData mapOrderEntryToOrderData(String id, OrderEntry orderEntry, List<InventoryEntry> inventoryEntries) {
        List<OrderContentEntry> contentEntry = orderEntry.getBeans();
        List<Bean> beans = buildBeanList(inventoryEntries, contentEntry);
        BigDecimal price = getTotalPrice(beans);

        OrderData orderData = new OrderData();

        orderData.setOrderedBy(orderEntry.getOrderedBy());
        orderData.setBeans(beans);
        orderData.setPrice(price);
        orderData.setId(id);

        return orderData;
    }

    /**
     * Takes order data from the client and prepares it
     * for insertion into the database by converting it into
     * an object that can be consumed by the JPA layer.
     * @param orderData
     * @param inventoryEntries
     * @return OrderEntry
     */
    public OrderEntry mapOrderDataToOrderEntry(OrderData orderData, List<InventoryEntry> inventoryEntries) {
        List<Bean> beans = orderData.getBeans();

        // The IDs of the order content entry and the order entry will be set by the JPA layer.
        List<OrderContentEntry> contentEntryList = buildOrderContentList(inventoryEntries, beans);
        OrderEntry orderEntry = buildOrderEntry(orderData, contentEntryList);
        
        return orderEntry;
    }

    private List<Bean> buildBeanList(List<InventoryEntry> inventory, List<OrderContentEntry> contentEntries) {
        List<Bean> beans = new ArrayList<>();
        
        for (OrderContentEntry content : contentEntries) {
            BeanType type = BeanType.getType(content.getBeanType());
            InventoryEntry beanData = findBeanDataByType(inventory, type);
            Bean bean = new Bean();

            if (beanData != null) {
                bean.setType(type);
                bean.setPricePerUnit(new BigDecimal(beanData.getPricePerUnit()));
                bean.setWeightPerUnit(new BigDecimal(beanData.getWeightPerUnit()));
                bean.setUnits(Integer.valueOf(content.getQuantity()));
            }

            beans.add(bean);
        }

        return beans;
    }

    private BigDecimal getTotalPrice(List<Bean> beans) {
        BigDecimal totalPrice = null;

        for (Bean bean : beans) {
            BigDecimal units = new BigDecimal(bean.getUnits());
            BigDecimal pricePerUnit = bean.getPricePerUnit();
            BigDecimal result = pricePerUnit.multiply(units);

            if (totalPrice == null) {
                totalPrice = result;
            }
            else  {
                totalPrice = totalPrice.add(result);
            }
        }

        return totalPrice;
    }

    private InventoryEntry findBeanDataByType(List<InventoryEntry> inventory, BeanType type) {
        for (InventoryEntry entry : inventory) {
            if (type.equals(entry.getBeanType())) {
                return entry;
            }
        }

        return null;
    }

    private List<OrderContentEntry> buildOrderContentList(List<InventoryEntry> inventory, List<Bean> beans) {
        List<OrderContentEntry> contentList = new ArrayList<>();

        for (Bean bean : beans) {
            BeanType type = bean.getType();
            InventoryEntry beanData = findBeanDataByType(inventory, type);
            OrderContentEntry contentEntry = new OrderContentEntry();

            if (beanData != null) {
                contentEntry.setBeanType(type.getName());
                contentEntry.setQuantity(bean.getUnits().toString());

                contentList.add(contentEntry);
            }
        }

        return contentList;
    }

    private OrderEntry buildOrderEntry(OrderData orderData, List<OrderContentEntry> orderContent) {
        OrderEntry orderEntry = new OrderEntry();

        for (OrderContentEntry content : orderContent) {
            content.setOrderEntry(orderEntry);
            orderEntry.addBean(content);
        }

        orderEntry.setOrderedBy(orderData.getOrderedBy());

        return orderEntry;
    }
}
