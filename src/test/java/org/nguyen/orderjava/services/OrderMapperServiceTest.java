package org.nguyen.orderjava.services;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.nguyen.orderjava.models.BeanType;
import org.nguyen.orderjava.models.OrderContentData;
import org.nguyen.orderjava.models.OrderData;
import org.nguyen.orderjava.models.jpa.InventoryEntry;
import org.nguyen.orderjava.models.jpa.OrderContentEntry;
import org.nguyen.orderjava.models.jpa.OrderEntry;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class OrderMapperServiceTest {

    @InjectMocks
    OrderMapperService orderMapperService;

    @Test
    void mapOrderEntryToOrderData_ShouldReturnOrderData_GivenOrderEntryAndBeanData() {
        OrderData expectedOrder = mockOrderData();
        OrderData result = orderMapperService.mapOrderEntryToOrderData("1", mockOrderEntry("1"), mockBeans());

        assertEquals(expectedOrder, result);
    }

    @Test
    void mapOrderDataToOrderEntry_ShouldReturnOrderEntry_GivenOrderDataAndBeanData() {
        OrderEntry expectedOrderEntry = mockOrderEntry(null);
        OrderEntry result = orderMapperService.mapOrderDataToOrderEntry(mockOrderData(), mockBeans());

        assertEquals(expectedOrderEntry, result);
    }

    private OrderContentData mockBean() {
        OrderContentData mock = new OrderContentData();

        mock.setQuantity(1);
        mock.setBeanType(BeanType.ARABICA);

        return mock;
    }

    private OrderData mockOrderData() {
        OrderData mock = new OrderData();
        List<OrderContentData> mockBeans = new ArrayList<>();

        mockBeans.add(mockBean());

        mock.setId("1");
        mock.setPrice(new BigDecimal("1.05"));
        mock.setBeans(mockBeans);
        mock.setOrderedBy("foo");

        return mock;
    }

    private OrderEntry mockOrderEntry(String id) {
        OrderEntry mock = new OrderEntry();

        if (id != null) {
            mock.setId(id);
        }

        mock.setOrderedBy("foo");
        mock.addBean(mockOrderContentEntry());

        return mock;
    }

    private OrderContentEntry mockOrderContentEntry() {
        OrderContentEntry mock = new OrderContentEntry();

        mock.setBeanType(BeanType.ARABICA.getName());
        mock.setQuantity("1");

        return mock;
    }

    private InventoryEntry mockBeanData() {
        InventoryEntry mock = new InventoryEntry();

        mock.setBeanType(BeanType.ARABICA);
        mock.setPricePerUnit("1.05");
        mock.setQuantity("10");
        mock.setWeightPerUnit("0.05");

        return mock;
    }

    private List<InventoryEntry> mockBeans() {
        List<InventoryEntry> mock = new ArrayList<>();

        mock.add(mockBeanData());

        return mock;
    }
}
