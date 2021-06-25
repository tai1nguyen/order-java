package org.nguyen.orderjava.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.nguyen.orderjava.models.BeanTypeEnum;
import org.nguyen.orderjava.models.dto.OrderContentDto;
import org.nguyen.orderjava.models.dto.OrderDto;
import org.nguyen.orderjava.models.dto.OrderUpdateDto;
import org.nguyen.orderjava.models.jpa.InventoryEntryJpa;
import org.nguyen.orderjava.models.jpa.OrderContentJpa;
import org.nguyen.orderjava.models.jpa.OrderJpa;

public class OrderMapper {

    private OrderMapper() {}

    /**
     * Maps an order entry from the database into an object that can be consumed by the client.
     * 
     * @param id
     * @param orderEntry
     * @param inventoryEntries
     * @return @Class{OrderDto}
     */
    public static OrderDto mapOrderEntryToOrderData(
            OrderJpa orderEntry,
            List<InventoryEntryJpa> inventoryEntries
    ) {
        List<OrderContentJpa> orderContentEntry = orderEntry.getContents();
        List<OrderContentDto> orderContentData = buildOrderContents(orderContentEntry);
        BigDecimal price = getTotalPrice(orderContentData, inventoryEntries);

        OrderDto orderData = new OrderDto();

        orderData.setOrderedBy(orderEntry.getOrderedBy());
        orderData.setContents(orderContentData);
        orderData.setPrice(price);
        orderData.setId(orderEntry.getId());

        return orderData;
    }

    /**
     * Takes order data from the client and prepares it for insertion into the database by
     * converting it into an object that can be consumed by the JPA layer.
     * 
     * @param orderData
     * @param inventoryEntries
     * @return @Class{OrderEntryJpa}
     */
    public static OrderJpa mapOrderDataToOrderEntry(OrderDto orderData) {
        List<OrderContentDto> beans = orderData.getContents();

        // The IDs of the order content entry and the order entry will be set by the JPA
        // layer.
        List<OrderContentJpa> contentEntryList = buildOrderContentEntries(beans);
        
        return buildOrderEntry(orderData, contentEntryList);
    }

    /**
     * Takes an order entry and updates it with the provided order update data.
     * 
     * @param orderEntry
     * @param update
     * @return @Class{OrderEntryJpa}
     */
    public static OrderJpa updateOrderEntry(OrderJpa orderEntry, OrderUpdateDto update) {
        List<OrderContentJpa> beanAdditions = buildOrderContentEntries(update.getContentAdditions());
        List<OrderContentJpa> beanUpdates = buildOrderContentEntries(update.getContentUpdates());

        // Add beans.
        for (OrderContentJpa beanAddition : beanAdditions) {
            orderEntry.addContent(beanAddition);
        }

        // Remove beans.
        for (OrderContentDto beanDeletion : update.getContentDeletions()) {
            orderEntry.removeContent(beanDeletion.getBeanType());
        }

        // Update beans.
        for (OrderContentJpa bean : orderEntry.getContents()) {
            OrderContentJpa beanUpdate = findOrderContentByBeanType(
                    BeanTypeEnum.getType(bean.getBeanType()),
                    beanUpdates
            );

            if (beanUpdate != null) {
                bean.setQuantity(beanUpdate.getQuantity());
            }
        }

        return orderEntry;
    }

    private static List<OrderContentDto> buildOrderContents(List<OrderContentJpa> contentEntries) {
        List<OrderContentDto> orderContents = new ArrayList<>();

        for (OrderContentJpa content : contentEntries) {
            BeanTypeEnum type = BeanTypeEnum.getType(content.getBeanType());
            OrderContentDto bean = new OrderContentDto(type, Integer.valueOf(content.getQuantity()));

            orderContents.add(bean);
        }

        return orderContents;
    }

    private static BigDecimal getTotalPrice(
            List<OrderContentDto> orderContent,
            List<InventoryEntryJpa> inventory
    ) {
        BigDecimal totalPrice = null;

        for (OrderContentDto bean : orderContent) {
            InventoryEntryJpa beanData = findBeanDataByType(inventory, bean.getBeanType());

            if (beanData == null) {
                continue;
            }

            BigDecimal units = new BigDecimal(bean.getQuantity());
            BigDecimal pricePerUnit = beanData.getPricePerUnit();
            BigDecimal result = pricePerUnit.multiply(units);

            if (totalPrice == null) {
                totalPrice = result;
            }
            else {
                totalPrice = totalPrice.add(result);
            }
        }

        return totalPrice;
    }

    private static List<OrderContentJpa> buildOrderContentEntries(List<OrderContentDto> beans) {
        List<OrderContentJpa> contentList = new ArrayList<>();

        if (beans != null) {
            for (OrderContentDto bean : beans) {
                BeanTypeEnum type = bean.getBeanType();
                OrderContentJpa contentEntry = new OrderContentJpa();

                contentEntry.setBeanType(type.getName());
                contentEntry.setQuantity(bean.getQuantity().toString());
                contentList.add(contentEntry);
            }
        }

        return contentList;
    }

    private static OrderJpa buildOrderEntry(OrderDto orderData, List<OrderContentJpa> orderContent) {
        OrderJpa orderEntry = new OrderJpa();

        if (orderContent != null) {
            for (OrderContentJpa content : orderContent) {
                orderEntry.addContent(content);
            }
        }

        orderEntry.setOrderedBy(orderData.getOrderedBy());

        return orderEntry;
    }

    private static OrderContentJpa findOrderContentByBeanType(
            BeanTypeEnum type,
            List<OrderContentJpa> orderContent
    ) {
        return orderContent.stream()
                .filter(suspect -> type.equals(BeanTypeEnum.getType(suspect.getBeanType())))
                .findAny()
                .orElse(null);
    }

    private static InventoryEntryJpa findBeanDataByType(
            List<InventoryEntryJpa> inventory,
            BeanTypeEnum type
    ) {
        return inventory.stream()
                .filter(suspect -> type.equals(suspect.getBeanType()))
                .findAny()
                .orElse(null);
    }
}
