package org.nguyen.orderjava.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.nguyen.orderjava.exceptions.OrderNotFoundException;
import org.nguyen.orderjava.models.BeanType;
import org.nguyen.orderjava.models.OrderData;
import org.nguyen.orderjava.models.jpa.InventoryEntry;
import org.nguyen.orderjava.models.jpa.OrderEntry;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;
    
    @Mock
    private OrderRepoService orderRepoService;

    @Mock
    private InventoryRepoService inventoryRepoService;

    @Mock
    private OrderMapperService orderMapperService;

    @Test
    void getOrderById_ShouldCallOrderRepoForData_GivenOrderId() throws OrderNotFoundException {
        when(orderRepoService.findOrderById("test")).thenReturn(mockOrderEntry());
        when(inventoryRepoService.findAllEntries()).thenReturn(mockInventoryList());

        orderService.getOrderById("test");

        verify(orderRepoService, times(1)).findOrderById("test");
    }

    @Test
    void getOrderById_ShouldCallInventoryRepoForData_GivenOrderId() throws OrderNotFoundException {
        when(orderRepoService.findOrderById("test")).thenReturn(mockOrderEntry());
        when(inventoryRepoService.findAllEntries()).thenReturn(mockInventoryList());

        orderService.getOrderById("test");

        verify(inventoryRepoService, times(1)).findAllEntries();
    }

    @Test
    void getOrderById_ShouldCallMapperServiceWithBeanAndOrderData_GivenOrderId() throws OrderNotFoundException {
        OrderEntry mockOrderEntry = mockOrderEntry();
        List<InventoryEntry> mockInventoryList = mockInventoryList();

        when(orderRepoService.findOrderById("test")).thenReturn(mockOrderEntry);
        when(inventoryRepoService.findAllEntries()).thenReturn(mockInventoryList);

        orderService.getOrderById("test");

        verify(orderMapperService, times(1)).mapOrderEntryToOrderData("test", mockOrderEntry, mockInventoryList);
    }

    @Test
    void getOrderById_ShouldThrowException_GivenNoOrderWasFound() throws OrderNotFoundException {
        Exception error = null;

        when(orderRepoService.findOrderById("test")).thenReturn(null);

        try {
            orderService.getOrderById("test");
        }
        catch (OrderNotFoundException ex)
        {
            error = ex;
        }

        verify(orderMapperService, times(0)).mapOrderEntryToOrderData(any(), any(), any());
        verify(inventoryRepoService, times(0)).findAllEntries();

        assertNotNull(error);
        assertTrue(error instanceof OrderNotFoundException);
    }

    @Test
    void saveOrder_ShouldReturnAnId_GivenSaveOperationSucceeded() {
        OrderEntry mock = mockOrderEntry();

        when(orderRepoService.saveOrder(any())).thenReturn(mock);
        when(inventoryRepoService.findAllEntries()).thenReturn(mockInventoryList());
        when(orderMapperService.mapOrderDataToOrderEntry(any(), any())).thenReturn(mock);

        String result = orderService.saveOrder(new OrderData());

        assertEquals("test", result);
    }

    private OrderEntry mockOrderEntry() {
        OrderEntry mock = new OrderEntry();

        mock.setId("test");

        return mock;
    }

    private List<InventoryEntry> mockInventoryList() {
        List<InventoryEntry> mock = new ArrayList<>();

        mock.add(mockInventoryEntry());

        return mock;
    }

    private InventoryEntry mockInventoryEntry() {
        InventoryEntry mock = new InventoryEntry();

        mock.setBeanType(BeanType.ARABICA);

        return mock;
    }
}
