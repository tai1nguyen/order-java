package org.nguyen.orderjava.services;

import static org.nguyen.orderjava.literals.Services.ORDER_MAPPER_SERVICE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.nguyen.orderjava.models.BeanTypeEnum;
import org.nguyen.orderjava.models.dto.OrderContentDto;
import org.nguyen.orderjava.models.dto.OrderDto;
import org.nguyen.orderjava.models.dto.OrderUpdateDto;
import org.nguyen.orderjava.models.jpa.InventoryEntryJpa;
import org.nguyen.orderjava.models.jpa.OrderContentJpa;
import org.nguyen.orderjava.models.jpa.OrderEntryJpa;
import org.springframework.stereotype.Service;

@Service(ORDER_MAPPER_SERVICE)
public class OrderMapperService {

    /**
     * Maps an order entry from the database into an object that can be consumed by the client.
     * 
     * @param id
     * @param orderEntry
     * @param inventoryEntries
     * @return @Class{OrderDto}
     */
    public OrderDto mapOrderEntryToOrderData(
            String id,
            OrderEntryJpa orderEntry,
            List<InventoryEntryJpa> inventoryEntries
    ) {
        List<OrderContentJpa> orderContentEntry = orderEntry.getBeans();
        List<OrderContentDto> orderContentData = buildOrderContents(orderContentEntry);
        BigDecimal price = getTotalPrice(orderContentData, inventoryEntries);

        OrderDto orderData = new OrderDto();

        orderData.setOrderedBy(orderEntry.getOrderedBy());
        orderData.setBeans(orderContentData);
        orderData.setPrice(price);
        orderData.setId(id);

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
    public OrderEntryJpa mapOrderDataToOrderEntry(OrderDto orderData) {
        List<OrderContentDto> beans = orderData.getBeans();

        // The IDs of the order content entry and the order entry will be set by the JPA
        // layer.
        List<OrderContentJpa> contentEntryList = buildOrderContentEntries(beans);
        OrderEntryJpa orderEntry = buildOrderEntry(orderData, contentEntryList);

        return orderEntry;
    }

    /**
     * Takes an order entry and updates it with the provided order update data.
     * 
     * @param orderEntry
     * @param update
     * @return @Class{OrderEntryJpa}
     */
    public OrderEntryJpa updateOrderEntry(OrderEntryJpa orderEntry, OrderUpdateDto update) {
        List<OrderContentJpa> beanAdditions = buildOrderContentEntries(update.getBeanAdditions());
        List<OrderContentJpa> beanUpdates = buildOrderContentEntries(update.getBeanUpdates());

        // Add beans.
        for (OrderContentJpa beanAddition : beanAdditions) {
            orderEntry.addBean(beanAddition);
        }

        // Remove beans.
        for (OrderContentDto beanDeletion : update.getBeanDeletions()) {
            orderEntry.removeBean(beanDeletion.getBeanType());
        }

        // Update beans.
        for (OrderContentJpa bean : orderEntry.getBeans()) {
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

    private List<OrderContentDto> buildOrderContents(List<OrderContentJpa> contentEntries) {
        List<OrderContentDto> orderContents = new ArrayList<>();

        for (OrderContentJpa content : contentEntries) {
            BeanTypeEnum type = BeanTypeEnum.getType(content.getBeanType());
            OrderContentDto bean = new OrderContentDto();

            bean.setBeanType(type);
            bean.setQuantity(Integer.valueOf(content.getQuantity()));
            orderContents.add(bean);
        }

        return orderContents;
    }

    private BigDecimal getTotalPrice(
            List<OrderContentDto> orderContent,
            List<InventoryEntryJpa> inventory
    ) {
        BigDecimal totalPrice = null;

        for (OrderContentDto bean : orderContent) {
            InventoryEntryJpa beanData = findBeanDataByType(inventory, bean.getBeanType());
            BigDecimal units = new BigDecimal(bean.getQuantity());
            BigDecimal pricePerUnit = new BigDecimal(beanData.getPricePerUnit());
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

    private List<OrderContentJpa> buildOrderContentEntries(List<OrderContentDto> beans) {
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

    private OrderEntryJpa buildOrderEntry(OrderDto orderData, List<OrderContentJpa> orderContent) {
        OrderEntryJpa orderEntry = new OrderEntryJpa();

        if (orderContent != null) {
            for (OrderContentJpa content : orderContent) {
                orderEntry.addBean(content);
            }
        }

        orderEntry.setOrderedBy(orderData.getOrderedBy());

        return orderEntry;
    }

    private OrderContentJpa findOrderContentByBeanType(
            BeanTypeEnum type,
            List<OrderContentJpa> orderContent
    ) {
        return orderContent.stream()
                .filter(suspect -> type.equals(BeanTypeEnum.getType(suspect.getBeanType())))
                .findAny()
                .orElse(null);
    }

    private InventoryEntryJpa findBeanDataByType(
            List<InventoryEntryJpa> inventory,
            BeanTypeEnum type
    ) {
        return inventory.stream()
                .filter(suspect -> type.equals(suspect.getBeanType()))
                .findAny()
                .orElse(null);
    }
}
