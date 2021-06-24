package org.nguyen.orderjava.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nguyen.orderjava.models.jpa.OrderEntryJpa;
import org.nguyen.orderjava.repositories.OrderRepository;

public class OrderRepoSeviceTest {

    private OrderRepoService orderRepoService;

    private OrderRepository orderRepo;

    @BeforeEach
    public void setUp() {
        this.orderRepo = mock(OrderRepository.class);
        this.orderRepoService = new OrderRepoService(this.orderRepo);
    }

    @Test
    public void Given_OrderDataForIdExists_When_AQueryIsMadeForOrderById_Then_OrderDataShouldBeReturned() {
        OrderEntryJpa expected = new OrderEntryJpa();
        expected.setId("1");
        when(orderRepo.findById("1")).thenReturn(Optional.of(expected));

        assertEquals(expected, orderRepoService.findOrderById("1"));
    }

    @Test
    public void Given_OrderDataForIdDoesNotExist_When_AQueryIsMadeForOrderById_Then_NoOrderDataShouldBeReturned() {
        Optional<OrderEntryJpa> mock = Optional.ofNullable(null);
        when(orderRepo.findById("1")).thenReturn(mock);

        assertNull(orderRepoService.findOrderById("1"));
    }

    @Test
    public void Given_OrderJpaExists_When_ATransactionToSaveTheOrderSuceeds_Then_TheOrderJpaShouldBeReturned() {
        OrderEntryJpa mock = new OrderEntryJpa();
        when(orderRepo.save(any())).thenReturn(mock);

        assertEquals(mock, orderRepoService.saveOrder(mock));
    }
}
