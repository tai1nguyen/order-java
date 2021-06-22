package org.nguyen.orderjava.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.nguyen.orderjava.models.jpa.OrderEntryJpa;
import org.nguyen.orderjava.repositories.OrderRepository;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderRepoSeviceTest {

    @InjectMocks
    OrderRepoService orderRepoService;

    @Mock
    OrderRepository orderRepo;

    @Test
    void findOrderById_ShouldReturnAnOrderEntry_GivenAnOrderEntryForTheIDExists() {
        OrderEntryJpa expected = new OrderEntryJpa();
        expected.setId("1");

        when(orderRepo.findById("1")).thenReturn(Optional.of(expected));

        OrderEntryJpa result = orderRepoService.findOrderById("1");

        assertEquals(expected, result);
    }

    @Test
    void findOrderById_ShouldReturnNull_GivenOrderEntryForTheIDDoesNotExist() {
        Optional<OrderEntryJpa> mock = Optional.ofNullable(null);

        when(orderRepo.findById("1")).thenReturn(mock);

        OrderEntryJpa result = orderRepoService.findOrderById("1");

        assertNull(result);
    }

    @Test
    void saveOrder_ShouldReturnAnOrderEntry_GivenTheSaveOperationSucceeded() {
        OrderEntryJpa mock = new OrderEntryJpa();

        when(orderRepo.save(any())).thenReturn(mock);

        OrderEntryJpa result = orderRepoService.saveOrder(mock);

        assertEquals(mock, result);
    }
}
