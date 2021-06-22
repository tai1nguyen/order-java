package org.nguyen.orderjava.controllers;

import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.nguyen.orderjava.models.BeanTypeEnum;
import org.nguyen.orderjava.models.jpa.InventoryEntryJpa;
import org.nguyen.orderjava.services.InventoryRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/inventory")
public class InventoryController {

    private final InventoryRepoService inventoryRepoService;

    @Autowired
    InventoryController(InventoryRepoService inventoryRepoService) {
        this.inventoryRepoService = inventoryRepoService;
    }

    @ApiOperation(
            value = "Get bean data",
            notes = "Get inventory details for the provided bean"
    )
    @GetMapping(value = "/bean")
    public InventoryEntryJpa getInventoryDataForBeanType(@RequestParam BeanTypeEnum beanType) {
        return inventoryRepoService.findEntryByType(beanType);
    }

    @ApiOperation(
            value = "Get all bean data",
            notes = "Get inventory details for all beans"
    )
    @GetMapping(value = "/beans")
    public List<InventoryEntryJpa> getInventory() {
        return inventoryRepoService.findAllEntries();
    }
}
