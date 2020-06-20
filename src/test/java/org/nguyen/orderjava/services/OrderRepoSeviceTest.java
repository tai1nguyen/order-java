package org.nguyen.orderjava.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.nguyen.orderjava.models.jpa.OrderEntry;
import org.nguyen.orderjava.repositories.OrderRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class OrderRepoSeviceTest {
    
    @InjectMocks
    OrderRepoService orderRepoService;

    @Mock
    OrderRepository orderRepo;

    @Test
    public void findOrderById_ShouldReturnAnOrderEntry_GivenAnOrderEntryForTheIDExists() {
        OrderEntry expected = new OrderEntry();
        expected.setId("1");

        when(orderRepo.findById("1")).thenReturn(Optional.of(expected));

        OrderEntry result = orderRepoService.findOrderById("1");

        assertEquals(expected, result);
    }

    @Test
    public void findOrderById_ShouldReturnNull_GivenOrderEntryForTheIDDoesNotExist() {
        Optional<OrderEntry> mock = Optional.ofNullable(null);

        when(orderRepo.findById("1")).thenReturn(mock);

        OrderEntry result = orderRepoService.findOrderById("1");

        assertNull(result);
    }

    @Test
    public void saveOrder_ShouldReturnAnOrderEntry_GivenTheSaveOperationSucceeded() {
        OrderEntry mock = new OrderEntry();

        when(orderRepo.save(any())).thenReturn(mock);

        OrderEntry result = orderRepoService.saveOrder(mock);

        assertEquals(mock, result);
    }
}
