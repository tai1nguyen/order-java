package org.nguyen.orderjava.services;

import java.util.Optional;

import org.nguyen.orderjava.repositories.OrderRepository;
import org.nguyen.orderjava.models.jpa.OrderEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderRepoServiceImpl implements OrderRepoService {

    private final OrderRepository orderRepo;

    @Autowired
    OrderRepoServiceImpl(OrderRepository orderRepository) {
        this.orderRepo = orderRepository;
    }

    @Override
    public OrderEntry getOrderById(String id) {
        OrderEntry orderEntry = null;
        Optional<OrderEntry> entry = orderRepo.findById(id);

        if (entry.isPresent()) {
            orderEntry = entry.get();
        }

        return orderEntry;
    }

    @Override
    public void deleteOrderById(String id) {
        // TODO Auto-generated method stub
    }

    @Override
    public void updateOrderById(String id) {
        // TODO Auto-generated method stub
    }
    
}