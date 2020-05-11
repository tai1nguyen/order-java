package org.nguyen.orderjava.services;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.nguyen.orderjava.models.Bean;
import org.nguyen.orderjava.models.OrderData;
import org.nguyen.orderjava.models.Bean.BeanType;
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
        List<Bean> expectedBeans = new ArrayList<>();
        OrderData expectedOrder = new OrderData();
        Bean expectedBean = new Bean();

        expectedBean.setPricePerUnit(new BigDecimal("1.05"));
        expectedBean.setWeightPerUnit(new BigDecimal("0.05"));
        expectedBean.setUnits(1);
        expectedBean.setType(BeanType.ARABICA);

        expectedBeans.add(expectedBean);

        expectedOrder.setId("1");
        expectedOrder.setPrice(new BigDecimal("1.05"));
        expectedOrder.setBeans(expectedBeans);

        OrderData result = orderMapperService.mapOrderEntryToOrderData("1", mockOrderEntry(), mockBeans());

        assertEquals(expectedOrder, result);
    }

    private OrderEntry mockOrderEntry() {
        OrderEntry mock = new OrderEntry();

        mock.setId("1");
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
