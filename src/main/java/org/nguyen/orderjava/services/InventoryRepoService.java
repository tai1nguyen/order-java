package org.nguyen.orderjava.services;

import static org.nguyen.orderjava.literals.Services.INVENTORY_REPO_SERVICE;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.nguyen.orderjava.models.Bean.BeanType;
import org.nguyen.orderjava.models.jpa.InventoryEntry;
import org.nguyen.orderjava.repositories.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(INVENTORY_REPO_SERVICE)
public class InventoryRepoService {

    private final InventoryRepository inventoryRepo;

    @Autowired
    InventoryRepoService(InventoryRepository inventoryRepository) {
        this.inventoryRepo = inventoryRepository;
    }

    public InventoryEntry getEntryByType(BeanType beanType) {
        InventoryEntry result = null;
        Optional<InventoryEntry> entry = inventoryRepo.findById(beanType.getName());

        if (entry.isPresent()) {
            result = entry.get();
        }

        return result;
    }

    public List<InventoryEntry> getAllEntries() {
        List<InventoryEntry> inventory = new ArrayList<InventoryEntry>();

        Iterable<InventoryEntry> iterator = inventoryRepo.findAll();

        for (InventoryEntry entry : iterator) {
            inventory.add(entry);
        }

        return inventory;
    }
}
