package org.nguyen.orderjava.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.nguyen.orderjava.models.BeanTypeEnum;
import org.nguyen.orderjava.models.jpa.InventoryEntryJpa;
import org.nguyen.orderjava.repositories.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryRepoService {

    private final InventoryRepository inventoryRepo;

    @Autowired
    InventoryRepoService(InventoryRepository inventoryRepository) {
        this.inventoryRepo = inventoryRepository;
    }

    public InventoryEntryJpa findEntryByType(BeanTypeEnum beanType) {
        InventoryEntryJpa result = null;
        Optional<InventoryEntryJpa> entry = inventoryRepo.findById(beanType.getName());

        if (entry.isPresent()) {
            result = entry.get();
        }

        return result;
    }

    public List<InventoryEntryJpa> findAllEntries() {
        List<InventoryEntryJpa> inventory = new ArrayList<InventoryEntryJpa>();

        Iterable<InventoryEntryJpa> iterator = inventoryRepo.findAll();

        for (InventoryEntryJpa entry : iterator) {
            inventory.add(entry);
        }

        return inventory;
    }
}
