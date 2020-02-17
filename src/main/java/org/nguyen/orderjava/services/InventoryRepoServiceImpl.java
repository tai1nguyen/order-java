package org.nguyen.orderjava.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.nguyen.orderjava.repositories.InventoryRepository;
import org.nguyen.orderjava.models.Bean.BeanType;
import org.nguyen.orderjava.models.jpa.InventoryEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryRepoServiceImpl implements InventoryRepoService {

    private final InventoryRepository inventoryRepo;

    @Autowired
    InventoryRepoServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepo = inventoryRepository;
    }

    @Override
    public InventoryEntry getBeanByType(BeanType beanType) {
        InventoryEntry result = null;
        Optional<InventoryEntry> entry = inventoryRepo.findById(beanType.getName());

        if (entry.isPresent()) {
            result = entry.get();
        }

        return result;
    }

    @Override
    public List<InventoryEntry> getAllBeans() {
        List<InventoryEntry> inventory = new ArrayList<InventoryEntry>();

        Iterable<InventoryEntry> iterator = inventoryRepo.findAll();

        for (InventoryEntry entry : iterator) {
            inventory.add(entry);
        }

        return inventory;
    }
}
