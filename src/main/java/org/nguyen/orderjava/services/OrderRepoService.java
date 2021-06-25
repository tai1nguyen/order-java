package org.nguyen.orderjava.services;

import java.util.Optional;

import org.nguyen.orderjava.models.jpa.OrderJpa;
import org.nguyen.orderjava.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderRepoService {

    private final OrderRepository orderRepo;

    @Autowired
    OrderRepoService(OrderRepository orderRepository) {
        this.orderRepo = orderRepository;
    }

    public OrderJpa findOrderById(String id) {
        OrderJpa orderEntry = null;
        Optional<OrderJpa> entry = orderRepo.findById(id);

        if (entry.isPresent()) {
            orderEntry = entry.get();
        }

        return orderEntry;
    }

    public void deleteOrderById(String id) {
        orderRepo.deleteById(id);
    }

    public OrderJpa saveOrder(OrderJpa orderEntry) {
        OrderJpa savedEntity = orderRepo.save(orderEntry);

        return savedEntity;
    }
}
