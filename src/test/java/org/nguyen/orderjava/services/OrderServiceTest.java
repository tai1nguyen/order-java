package org.nguyen.orderjava.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nguyen.orderjava.exceptions.OrderNotFoundException;
import org.nguyen.orderjava.models.BeanTypeEnum;
import org.nguyen.orderjava.models.dto.OrderDto;
import org.nguyen.orderjava.models.dto.OrderUpdateDto;
import org.nguyen.orderjava.models.jpa.InventoryEntryJpa;
import org.nguyen.orderjava.models.jpa.OrderEntryJpa;
import org.springframework.dao.EmptyResultDataAccessException;

public class OrderServiceTest {

    private OrderService orderService;

    private OrderRepoService orderRepoService;

    private InventoryRepoService inventoryRepoService;

    private OrderMapperService orderMapperService;

    @BeforeEach
    public void setUp() {
        this.orderRepoService = mock(OrderRepoService.class);
        this.inventoryRepoService = mock(InventoryRepoService.class);
        this.orderMapperService = mock(OrderMapperService.class);
        this.orderService = new OrderService(
                this.orderRepoService,
                this.inventoryRepoService,
                this.orderMapperService
        );
    }

    @Test
    public void Given_OrderIdAndOrderExists_When_ARequestIsMadeForOrderById_Then_OrderDataShouldBeFetched() {
        when(orderRepoService.findOrderById("test")).thenReturn(mockOrderEntry());
        when(inventoryRepoService.findAllEntries()).thenReturn(mockInventoryList());

        orderService.getOrderById("test");

        verify(orderRepoService, times(1)).findOrderById("test");
    }

    @Test
    public void Given_OrderIdAndOrderExists_When_ARequestIsMadeForOrderById_Then_BeanDataShouldBeFetched() {
        when(orderRepoService.findOrderById("test")).thenReturn(mockOrderEntry());
        when(inventoryRepoService.findAllEntries()).thenReturn(mockInventoryList());

        orderService.getOrderById("test");

        verify(inventoryRepoService, times(1)).findAllEntries();
    }

    @Test
    public void Given_OrderIdAndOrderExists_When_ARequestIsMadeForOrderById_Then_OrderDataAndBeanDataShouldBeMappedToOrderJpa() {
        OrderEntryJpa mockOrderEntry = mockOrderEntry();
        List<InventoryEntryJpa> mockInventoryList = mockInventoryList();
        when(orderRepoService.findOrderById("test")).thenReturn(mockOrderEntry);
        when(inventoryRepoService.findAllEntries()).thenReturn(mockInventoryList);

        orderService.getOrderById("test");

        verify(orderMapperService, times(1))
                .mapOrderEntryToOrderData("test", mockOrderEntry, mockInventoryList);
    }

    @Test
    public void Given_OrderIdAndOrderDoesNotExist_When_ARequestIsMadeForOrderById_Then_ExceptionShouldBeThrown() {
        when(orderRepoService.findOrderById("test")).thenReturn(null);

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.getOrderById("test");
        });

        verify(orderMapperService, times(0)).mapOrderEntryToOrderData(any(), any(), any());
        verify(inventoryRepoService, times(0)).findAllEntries();
    }

    @Test
    public void Given_OrderDtoAndSaveOperationWillSucceed_When_ARequestIsMadeToSaveOrderById_Then_OrderIdWillBeReturned() {
        OrderEntryJpa mock = mockOrderEntry();
        when(orderRepoService.saveOrder(any())).thenReturn(mock);
        when(inventoryRepoService.findAllEntries()).thenReturn(mockInventoryList());
        when(orderMapperService.mapOrderDataToOrderEntry(any())).thenReturn(mock);

        assertEquals("test", orderService.saveOrder(new OrderDto()));
    }

    @Test
    public void Given_OrderUpdateDtoAndOrderIdAndOrderExists_When_ARequestIsMadeToUpdateOrder_Then_MapperServiceShouldBeCalledToUpdateTheOrder() {
        OrderEntryJpa mock = mockOrderEntry();
        OrderUpdateDto mockUpdateData = new OrderUpdateDto();
        when(orderRepoService.findOrderById("test")).thenReturn(mock);
        when(orderMapperService.updateOrderEntry(any(), any())).thenReturn(mock);
        when(orderRepoService.saveOrder(any())).thenReturn(mock);

        orderService.updateOrder("test", mockUpdateData);

        verify(orderMapperService, times(1)).updateOrderEntry(mock, mockUpdateData);
    }

    @Test
    public void Given_OrderUpdateDtoAndOrderIdAndOrderExistsAndUpdateSucceeds_When_ARequestIsMadeToUpdateOrder_Then_OrderIdShouldBeReturned() {
        OrderEntryJpa mock = mockOrderEntry();
        OrderUpdateDto mockUpdateData = new OrderUpdateDto();

        when(orderRepoService.findOrderById("test")).thenReturn(mock);
        when(orderMapperService.updateOrderEntry(any(), any())).thenReturn(mock);
        when(orderRepoService.saveOrder(any())).thenReturn(mock);

        assertEquals("test", orderService.updateOrder("test", mockUpdateData));
    }

    @Test
    public void Given_OrderDoesNotExist_When_ARequestIsMadeToUpdateOrder_Then_ExceptionShouldBeThrown() {
        OrderUpdateDto mockUpdateData = new OrderUpdateDto();
        when(orderRepoService.findOrderById("test")).thenReturn(null);

        OrderNotFoundException error = assertThrows(OrderNotFoundException.class, () -> {
            orderService.updateOrder("test", mockUpdateData);
        });

        assertEquals(new OrderNotFoundException("test").getMessage(), error.getMessage());
    }

    @Test
    public void Given_OrderDoesNotExist_When_ARequestIsMadeToDeleteOrder_Then_ExceptionShouldBeThrown() {
        doThrow(new EmptyResultDataAccessException(0)).when(orderRepoService).deleteOrderById("1");

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.deleteOrder("1");
        });
    }

    private OrderEntryJpa mockOrderEntry() {
        OrderEntryJpa mock = new OrderEntryJpa();

        mock.setId("test");

        return mock;
    }

    private List<InventoryEntryJpa> mockInventoryList() {
        List<InventoryEntryJpa> mock = new ArrayList<>();

        mock.add(mockInventoryEntry());

        return mock;
    }

    private InventoryEntryJpa mockInventoryEntry() {
        InventoryEntryJpa mock = new InventoryEntryJpa();

        mock.setBeanType(BeanTypeEnum.ARABICA);

        return mock;
    }
}
