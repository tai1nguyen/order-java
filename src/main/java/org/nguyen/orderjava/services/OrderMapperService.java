package org.nguyen.orderjava.services;

import static org.nguyen.orderjava.literals.Services.ORDER_MAPPER_SERVICE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.nguyen.orderjava.models.BeanType;
import org.nguyen.orderjava.models.OrderContentData;
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
        List<OrderContentEntry> orderContentEntry = orderEntry.getBeans();
        List<OrderContentData> orderContentData = buildOrderContents(inventoryEntries, orderContentEntry);
        BigDecimal price = getTotalPrice(orderContentData, inventoryEntries);

        OrderData orderData = new OrderData();

        orderData.setOrderedBy(orderEntry.getOrderedBy());
        orderData.setBeans(orderContentData);
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
        List<OrderContentData> beans = orderData.getBeans();

        // The IDs of the order content entry and the order entry will be set by the JPA layer.
        List<OrderContentEntry> contentEntryList = buildOrderContentEntries(inventoryEntries, beans);
        OrderEntry orderEntry = buildOrderEntry(orderData, contentEntryList);
        
        return orderEntry;
    }

    private List<OrderContentData> buildOrderContents(List<InventoryEntry> inventory, List<OrderContentEntry> contentEntries) {
        List<OrderContentData> orderContents = new ArrayList<>();
        
        for (OrderContentEntry content : contentEntries) {
            BeanType type = BeanType.getType(content.getBeanType());
            InventoryEntry beanData = findBeanDataByType(inventory, type);
            OrderContentData bean = new OrderContentData();

            if (beanData != null) {
                bean.setBeanType(type);
                // bean.setPricePerUnit(new BigDecimal(beanData.getPricePerUnit()));
                // bean.setWeightPerUnit(new BigDecimal(beanData.getWeightPerUnit()));
                bean.setQuantity(Integer.valueOf(content.getQuantity()));
            }

            orderContents.add(bean);
        }

        return orderContents;
    }

    private BigDecimal getTotalPrice(List<OrderContentData> orderContent, List<InventoryEntry> inventory) {
        BigDecimal totalPrice = null;

        for (OrderContentData bean : orderContent) {
            InventoryEntry beanData = findBeanDataByType(inventory, bean.getBeanType());
            BigDecimal units = new BigDecimal(bean.getQuantity());
            BigDecimal pricePerUnit = new BigDecimal(beanData.getPricePerUnit());
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

    private List<OrderContentEntry> buildOrderContentEntries(List<InventoryEntry> inventory, List<OrderContentData> beans) {
        List<OrderContentEntry> contentList = new ArrayList<>();

        for (OrderContentData bean : beans) {
            BeanType type = bean.getBeanType();
            InventoryEntry beanData = findBeanDataByType(inventory, type);
            OrderContentEntry contentEntry = new OrderContentEntry();

            if (beanData != null) {
                contentEntry.setBeanType(type.getName());
                contentEntry.setQuantity(bean.getQuantity().toString());

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
