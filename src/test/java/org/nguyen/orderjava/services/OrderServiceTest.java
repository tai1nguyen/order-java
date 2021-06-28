package org.nguyen.orderjava.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nguyen.orderjava.exceptions.OrderNotFoundException;
import org.nguyen.orderjava.models.BeanTypeEnum;
import org.nguyen.orderjava.models.dto.OrderContentDto;
import org.nguyen.orderjava.models.dto.OrderDto;
import org.nguyen.orderjava.models.dto.OrderUpdateDto;
import org.nguyen.orderjava.models.jpa.InventoryEntryJpa;
import org.nguyen.orderjava.models.jpa.OrderContentJpa;
import org.nguyen.orderjava.models.jpa.OrderJpa;

class OrderServiceTest {

    private OrderService orderService;

    private OrderRepoService orderRepoService;

    private InventoryRepoService inventoryRepoService;

    @BeforeEach
    public void setUp() {
        this.orderRepoService = mock(OrderRepoService.class);
        this.inventoryRepoService = mock(InventoryRepoService.class);
        this.orderService = new OrderService(this.orderRepoService, this.inventoryRepoService);
    }

    @Test
    void Given_OrderExists_When_ARequestForOrderByIdIsMade_Then_OrderShouldBeReturned() {
        OrderDto expected = new OrderDto();
        OrderContentDto expectedContent = new OrderContentDto(BeanTypeEnum.ARABICA, 50);
        expected.setContents(Arrays.asList(expectedContent));
        expected.setComplete(false);
        expected.setPrice(new BigDecimal("50.0"));
        expected.setOrderedBy("foo");
        expected.setId("1");
        when(orderRepoService.findOrderById("1")).thenReturn(mockOrderEntry("1"));
        when(inventoryRepoService.findAllEntries()).thenReturn(mockInventoryList());

        assertEquals(expected, orderService.getOrderById("1"));
    }

    @Test
    void Given_OrderDoesNotExist_When_ARequestForOrderByIdIsMade_Then_ExceptionShouldBeThrown() {
        when(orderRepoService.findOrderById("1")).thenReturn(null);
        when(inventoryRepoService.findAllEntries()).thenReturn(mockInventoryList());

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.getOrderById("1");
        });
    }

    @Test
    void Given_OrderExists_When_ARequestToUpdateOrderIsMade_Then_UpdatedOrderShouldBeReturned() {
        OrderUpdateDto update = new OrderUpdateDto();
        OrderJpa orderEntry = mockOrderEntry("1");
        OrderContentJpa orderContent = new OrderContentJpa();
        OrderDto expected = new OrderDto();
        OrderContentDto expectedLiberian = new OrderContentDto(BeanTypeEnum.LIBERIAN, 5);
        OrderContentDto expectedExcelsa = new OrderContentDto(BeanTypeEnum.EXCELSA, 1);
        expected.setContents(Arrays.asList(expectedLiberian, expectedExcelsa));
        expected.setId("1");
        expected.setOrderedBy("foo");
        expected.setPrice(new BigDecimal("6.0"));
        orderContent.setBeanType(BeanTypeEnum.LIBERIAN.getName());
        orderContent.setQuantity(3);
        orderEntry.addContent(orderContent);
        update.setId("1");
        update.setContentAdditions(Arrays.asList(new OrderContentDto(BeanTypeEnum.EXCELSA, 1)));
        update.setContentDeletions(Arrays.asList(new OrderContentDto(BeanTypeEnum.ARABICA, null)));
        update.setContentUpdates(Arrays.asList(new OrderContentDto(BeanTypeEnum.LIBERIAN, 5)));
        when(orderRepoService.findOrderById("1")).thenReturn(orderEntry);
        when(inventoryRepoService.findAllEntries()).thenReturn(mockInventoryList());
        when(orderRepoService.saveOrder(orderEntry)).thenReturn(orderEntry);

        OrderDto result = orderService.updateOrder(update.getId(), update);

        assertEquals(expected, result);
    }

    @Test
    void Given_OrderDoesNotExist_When_ARequestToUpdateOrderIsMade_Then_ExceptionShouldBeThrown() {
        OrderUpdateDto update = new OrderUpdateDto();
        when(orderRepoService.findOrderById("1")).thenReturn(null);
        when(inventoryRepoService.findAllEntries()).thenReturn(mockInventoryList());

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.updateOrder("1", update);
        });
    }

    @Test
    void Given_OrderDoesNotExist_When_ARequestToDeleteOrderIsMade_Then_ExceptionShouldBeThrown() {
        doThrow(new OrderNotFoundException(null)).when(orderRepoService).deleteOrderById("1");

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.deleteOrder("1");
        });
    }

    @Test
    void Given_OrderData_When_ARequestToSaveOrderIsMade_Then_OrderShouldBeReturned() {
        OrderDto order = new OrderDto();
        OrderContentDto orderContent = new OrderContentDto(BeanTypeEnum.ARABICA, 50);
        order.setContents(Arrays.asList(orderContent));
        order.setPrice(new BigDecimal("50.0"));
        when(orderRepoService.saveOrder(any(OrderJpa.class))).then(returnsFirstArg());
        when(inventoryRepoService.findAllEntries()).thenReturn(mockInventoryList());

        assertEquals(order, orderService.saveOrder(order));
    }

    private OrderJpa mockOrderEntry(String id) {
        OrderJpa mock = new OrderJpa();
        OrderContentJpa mockContent = new OrderContentJpa();

        mockContent.setBeanType(BeanTypeEnum.ARABICA.getName());
        mockContent.setQuantity(50);
        mock.setId(id);
        mock.setOrderedBy("foo");
        mock.addContent(mockContent);

        return mock;
    }

    private List<InventoryEntryJpa> mockInventoryList() {
        List<InventoryEntryJpa> mock = new ArrayList<>();

        mock.add(mockInventoryEntry(BeanTypeEnum.ARABICA));
        mock.add(mockInventoryEntry(BeanTypeEnum.EXCELSA));
        mock.add(mockInventoryEntry(BeanTypeEnum.LIBERIAN));

        return mock;
    }

    private InventoryEntryJpa mockInventoryEntry(BeanTypeEnum beanType) {
        InventoryEntryJpa mock = new InventoryEntryJpa(
            beanType,
            new BigDecimal("0.5"),
            new BigDecimal("1.0"),
            100
        );

        return mock;
    }
}
