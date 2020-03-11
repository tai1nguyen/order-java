package org.nguyen.orderjava.services;

import static org.nguyen.orderjava.literals.Services.ORDER_REPO_SERVICE;

import java.util.Optional;

import org.nguyen.orderjava.models.jpa.OrderEntry;
import org.nguyen.orderjava.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(ORDER_REPO_SERVICE)
public class OrderRepoService {

    private final OrderRepository orderRepo;

    @Autowired
    OrderRepoService(OrderRepository orderRepository) {
        this.orderRepo = orderRepository;
    }

    public OrderEntry findOrderById(String id) {
        OrderEntry orderEntry = null;
        Optional<OrderEntry> entry = orderRepo.findById(id);

        if (entry.isPresent()) {
            orderEntry = entry.get();
        }

        return orderEntry;
    }
}
