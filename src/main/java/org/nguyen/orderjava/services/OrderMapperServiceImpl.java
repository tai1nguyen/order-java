package org.nguyen.orderjava.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.nguyen.orderjava.models.Bean;
import org.nguyen.orderjava.models.OrderData;
import org.nguyen.orderjava.models.Bean.BeanType;
import org.nguyen.orderjava.models.jpa.InventoryEntry;
import org.nguyen.orderjava.models.jpa.OrderContentEntry;
import org.nguyen.orderjava.models.jpa.OrderEntry;
import org.springframework.stereotype.Service;

@Service
public class OrderMapperServiceImpl implements OrderMapperService {

    @Override
    public OrderData mapToOrderData(OrderEntry orderEntry, List<InventoryEntry> inventoryEntries) {
        List<OrderContentEntry> contentEntry = orderEntry.getBeans();
        List<Bean> beans = buildBeansList(inventoryEntries, contentEntry);
        BigDecimal price = getTotalPrice(beans);

        OrderData orderData = new OrderData();

        orderData.setBeans(beans);
        orderData.setPrice(price);

        return orderData;
    }

    private List<Bean> buildBeansList(List<InventoryEntry> inventory, List<OrderContentEntry> contentEntries) {
        List<Bean> beans = new ArrayList<Bean>();
        
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
}