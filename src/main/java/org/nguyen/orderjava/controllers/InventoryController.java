package org.nguyen.orderjava.controllers;

import java.util.List;

import org.nguyen.orderjava.models.BeanType;
import org.nguyen.orderjava.models.jpa.InventoryEntry;
import org.nguyen.orderjava.services.InventoryRepoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryRepoService inventoryRepoService;

    InventoryController(InventoryRepoService inventoryRepoService) {
        this.inventoryRepoService = inventoryRepoService;
    }

    @GetMapping("/bean")
    public InventoryEntry getInventoryDataForBeanType(@RequestParam String type) {
        InventoryEntry entry = inventoryRepoService.findEntryByType(BeanType.getType(type));

        return entry;
    }

    @GetMapping("/beans")
    public List<InventoryEntry> getInventory() {
        return inventoryRepoService.findAllEntries();
    }
}
