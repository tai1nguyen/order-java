package org.nguyen.orderjava.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nguyen.orderjava.models.jpa.OrderJpa;
import org.nguyen.orderjava.repositories.OrderRepository;

class OrderRepoSeviceTest {

    private OrderRepoService orderRepoService;

    private OrderRepository orderRepo;

    @BeforeEach
    public void setUp() {
        this.orderRepo = mock(OrderRepository.class);
        this.orderRepoService = new OrderRepoService(this.orderRepo);
    }

    @Test
    void Given_OrderDataForIdExists_When_AQueryIsMadeForOrderById_Then_OrderDataShouldBeReturned() {
        OrderJpa expected = new OrderJpa();
        expected.setId("1");
        when(orderRepo.findById("1")).thenReturn(Optional.of(expected));

        assertEquals(expected, orderRepoService.findOrderById("1"));
    }

    @Test
    void Given_OrderDataForIdDoesNotExist_When_AQueryIsMadeForOrderById_Then_NoOrderDataShouldBeReturned() {
        Optional<OrderJpa> mock = Optional.ofNullable(null);
        when(orderRepo.findById("1")).thenReturn(mock);

        assertNull(orderRepoService.findOrderById("1"));
    }

    @Test
    void Given_OrderJpaExists_When_ATransactionToSaveTheOrderSuceeds_Then_TheOrderJpaShouldBeReturned() {
        OrderJpa mock = new OrderJpa();
        when(orderRepo.save(any())).thenReturn(mock);

        assertEquals(mock, orderRepoService.saveOrder(mock));
    }
}
