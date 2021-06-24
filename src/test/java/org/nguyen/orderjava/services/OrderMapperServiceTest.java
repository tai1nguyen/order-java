package org.nguyen.orderjava.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nguyen.orderjava.models.BeanTypeEnum;
import org.nguyen.orderjava.models.dto.OrderContentDto;
import org.nguyen.orderjava.models.dto.OrderDto;
import org.nguyen.orderjava.models.dto.OrderUpdateDto;
import org.nguyen.orderjava.models.jpa.InventoryEntryJpa;
import org.nguyen.orderjava.models.jpa.OrderContentJpa;
import org.nguyen.orderjava.models.jpa.OrderEntryJpa;

public class OrderMapperServiceTest {

    private OrderMapperService mapperService;

    @BeforeEach
    public void setUp() {
        this.mapperService = new OrderMapperService();
    }

    @Test
    public void Given_OrderAndBeanDataExists_When_AskedToMapOrderJpaToOrderDto_Then_MappedOrderDtoShouldBeReturned() {
        OrderDto expectedOrder = mockOrderData();
        List<OrderContentDto> expectedOrderBeans = new ArrayList<>();
        OrderEntryJpa mockOrderEntry = mockOrderEntry("1");
        expectedOrderBeans.add(mockOrderContentData(BeanTypeEnum.ARABICA, "1"));
        expectedOrder.setBeans(expectedOrderBeans);
        mockOrderEntry.addBean(mockOrderContentEntry(BeanTypeEnum.ARABICA, "1"));

        OrderDto result = mapperService.mapOrderEntryToOrderData("1", mockOrderEntry, mockBeans());

        assertEquals(expectedOrder, result);
    }

    @Test
    public void Given_BeanDataExists_When_AskedToMapOrderDtoToOrderJpa_Then_MappedOrderJpaShouldBeReturned() {
        OrderEntryJpa expectedOrderEntry = mockOrderEntry(null);
        OrderDto mockOrderData = mockOrderData();
        List<OrderContentDto> mockOrderDataBeans = new ArrayList<>();
        expectedOrderEntry.addBean(mockOrderContentEntry(BeanTypeEnum.ARABICA, "1"));
        mockOrderDataBeans.add(mockOrderContentData(BeanTypeEnum.ARABICA, "1"));
        mockOrderData.setBeans(mockOrderDataBeans);

        OrderEntryJpa result = mapperService.mapOrderDataToOrderEntry(mockOrderData);

        assertEquals(expectedOrderEntry, result);
    }

    @Test
    public void Given_OrderUpdateDtoAndOrderJpa_When_AskedToUpdateOrderJpa_Then_ShouldReturnUpdatedOrderJpa() {
        OrderEntryJpa mock = mockOrderEntry("1");
        OrderEntryJpa expected = mockOrderEntry("1");
        OrderUpdateDto udpateData = mockOrderUpdateData();
        mock.addBean(mockOrderContentEntry(BeanTypeEnum.ARABICA, "1"));
        mock.addBean(mockOrderContentEntry(BeanTypeEnum.EXCELSA, "2"));
        expected.addBean(mockOrderContentEntry(BeanTypeEnum.EXCELSA, "4"));
        expected.addBean(mockOrderContentEntry(BeanTypeEnum.LIBERIAN, "3"));

        OrderEntryJpa result = mapperService.updateOrderEntry(mock, udpateData);

        assertEquals(expected, result);
    }

    private OrderUpdateDto mockOrderUpdateData() {
        OrderUpdateDto mock = new OrderUpdateDto();
        List<OrderContentDto> additions = new ArrayList<>();
        List<OrderContentDto> deletions = new ArrayList<>();
        List<OrderContentDto> updates = new ArrayList<>();

        additions.add(mockOrderContentData(BeanTypeEnum.LIBERIAN, "3"));
        deletions.add(mockOrderContentData(BeanTypeEnum.ARABICA, null));
        updates.add(mockOrderContentData(BeanTypeEnum.EXCELSA, "4"));

        mock.setBeanAdditions(additions);
        mock.setBeanDeletions(deletions);
        mock.setBeanUpdates(updates);

        return mock;
    }

    private OrderContentDto mockOrderContentData(BeanTypeEnum type, String quantity) {
        OrderContentDto mock = new OrderContentDto();

        mock.setBeanType(type);

        if (quantity != null) {
            mock.setQuantity(Integer.parseInt(quantity));
        }

        return mock;
    }

    private OrderDto mockOrderData() {
        OrderDto mock = new OrderDto();

        mock.setId("1");
        mock.setPrice(new BigDecimal("1.05"));
        mock.setOrderedBy("foo");

        return mock;
    }

    private OrderEntryJpa mockOrderEntry(String id) {
        OrderEntryJpa mock = new OrderEntryJpa();

        if (id != null) {
            mock.setId(id);
        }

        mock.setOrderedBy("foo");

        return mock;
    }

    private OrderContentJpa mockOrderContentEntry(BeanTypeEnum type, String quantity) {
        OrderContentJpa mock = new OrderContentJpa();

        mock.setBeanType(type.getName());
        mock.setQuantity(quantity);

        return mock;
    }

    private InventoryEntryJpa mockBeanData() {
        InventoryEntryJpa mock = new InventoryEntryJpa();

        mock.setBeanType(BeanTypeEnum.ARABICA);
        mock.setPricePerUnit("1.05");
        mock.setQuantity("10");
        mock.setWeightPerUnit("0.05");

        return mock;
    }

    private List<InventoryEntryJpa> mockBeans() {
        List<InventoryEntryJpa> mock = new ArrayList<>();

        mock.add(mockBeanData());

        return mock;
    }
}
