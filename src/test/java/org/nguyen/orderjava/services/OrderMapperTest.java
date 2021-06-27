package org.nguyen.orderjava.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.nguyen.orderjava.models.BeanTypeEnum;
import org.nguyen.orderjava.models.dto.OrderContentDto;
import org.nguyen.orderjava.models.dto.OrderDto;
import org.nguyen.orderjava.models.dto.OrderUpdateDto;
import org.nguyen.orderjava.models.jpa.InventoryEntryJpa;
import org.nguyen.orderjava.models.jpa.OrderContentJpa;
import org.nguyen.orderjava.models.jpa.OrderJpa;

class OrderMapperTest {

    @Test
    void Given_OrderAndBeanDataExists_When_AskedToMapOrderJpaToOrderDto_Then_MappedOrderDtoShouldBeReturned() {
        OrderDto expectedOrder = mockOrderData();
        List<OrderContentDto> expectedOrderBeans = new ArrayList<>();
        OrderJpa mockOrderEntry = mockOrderEntry("1");
        expectedOrderBeans.add(new OrderContentDto(BeanTypeEnum.ARABICA, 1));
        expectedOrder.setContents(expectedOrderBeans);
        mockOrderEntry.addContent(new OrderContentJpa(BeanTypeEnum.ARABICA, 1));

        OrderDto result = OrderMapper.mapOrderEntryToOrderData(mockOrderEntry, mockBeans());

        assertEquals(expectedOrder, result);
    }

    @Test
    void Given_BeanDataExists_When_AskedToMapOrderDtoToOrderJpa_Then_MappedOrderJpaShouldBeReturned() {
        OrderJpa expectedOrderEntry = mockOrderEntry(null);
        OrderDto mockOrderData = mockOrderData();
        List<OrderContentDto> mockOrderDataBeans = new ArrayList<>();
        expectedOrderEntry.addContent(new OrderContentJpa(BeanTypeEnum.ARABICA, 1));
        mockOrderDataBeans.add(new OrderContentDto(BeanTypeEnum.ARABICA, 1));
        mockOrderData.setContents(mockOrderDataBeans);

        OrderJpa result = OrderMapper.mapOrderDataToOrderEntry(mockOrderData);

        assertEquals(expectedOrderEntry, result);
    }

    @Test
    void Given_OrderUpdateDtoAndOrderJpa_When_AskedToUpdateOrderJpa_Then_ShouldReturnUpdatedOrderJpa() {
        OrderJpa mock = mockOrderEntry("1");
        OrderJpa expected = mockOrderEntry("1");
        OrderUpdateDto udpateData = mockOrderUpdateData();
        mock.addContent(new OrderContentJpa(BeanTypeEnum.ARABICA, 1));
        mock.addContent(new OrderContentJpa(BeanTypeEnum.EXCELSA, 2));
        expected.addContent(new OrderContentJpa(BeanTypeEnum.EXCELSA, 4));
        expected.addContent(new OrderContentJpa(BeanTypeEnum.LIBERIAN, 3));

        OrderJpa result = OrderMapper.updateOrderEntry(mock, udpateData);

        assertEquals(expected, result);
    }

    private OrderUpdateDto mockOrderUpdateData() {
        OrderUpdateDto mock = new OrderUpdateDto();
        List<OrderContentDto> additions = new ArrayList<>();
        List<OrderContentDto> deletions = new ArrayList<>();
        List<OrderContentDto> updates = new ArrayList<>();

        additions.add(new OrderContentDto(BeanTypeEnum.LIBERIAN, 3));
        deletions.add(new OrderContentDto(BeanTypeEnum.ARABICA, null));
        updates.add(new OrderContentDto(BeanTypeEnum.EXCELSA, 4));

        mock.setContentAdditions(additions);
        mock.setContentDeletions(deletions);
        mock.setContentUpdates(updates);

        return mock;
    }

    private OrderDto mockOrderData() {
        OrderDto mock = new OrderDto();

        mock.setId("1");
        mock.setPrice(new BigDecimal("1.05"));
        mock.setOrderedBy("foo");

        return mock;
    }

    private OrderJpa mockOrderEntry(String id) {
        OrderJpa mock = new OrderJpa();

        if (id != null) {
            mock.setId(id);
        }

        mock.setOrderedBy("foo");

        return mock;
    }

    private InventoryEntryJpa mockBeanData() {
        InventoryEntryJpa mock = new InventoryEntryJpa(
                BeanTypeEnum.ARABICA,
                new BigDecimal("0.05"),
                new BigDecimal("1.05"),
                10
        );

        return mock;
    }

    private List<InventoryEntryJpa> mockBeans() {
        List<InventoryEntryJpa> mock = new ArrayList<>();

        mock.add(mockBeanData());

        return mock;
    }
}
