package org.nguyen.orderjava.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nguyen.orderjava.models.BeanTypeEnum;
import org.nguyen.orderjava.models.jpa.InventoryEntryJpa;
import org.nguyen.orderjava.repositories.InventoryRepository;

public class InventoryRepoServiceTest {

    private InventoryRepoService inventoryRepoService;

    private InventoryRepository inventoryRepository;

    @BeforeEach
    public void setUp() {
        this.inventoryRepository = mock(InventoryRepository.class);
        this.inventoryRepoService = new InventoryRepoService(this.inventoryRepository);
    }

    @Test
    public void Given_BeanDataExists_When_AQueryForBeanByTypeIsMade_Then_BeanDataShouldBeReturned() {
        InventoryEntryJpa entry = new InventoryEntryJpa();
        Optional<InventoryEntryJpa> mock = null;
        entry.setBeanType(BeanTypeEnum.ARABICA);
        entry.setPricePerUnit("0");
        entry.setWeightPerUnit("0");
        entry.setQuantity("0");
        mock = Optional.of(entry);
        when(inventoryRepository.findById(BeanTypeEnum.ARABICA.getName())).thenReturn(mock);

        InventoryEntryJpa result = inventoryRepoService.findEntryByType(BeanTypeEnum.ARABICA);

        assertEquals(entry, result);
    }

    @Test
    public void Given_BeanDataDoesNotExist_When_AQueryForBeanByTypeIsMade_Then_NoDataShouldBeReturned() {
        Optional<InventoryEntryJpa> mock = Optional.ofNullable(null);
        when(inventoryRepository.findById(BeanTypeEnum.ARABICA.getName())).thenReturn(mock);

        InventoryEntryJpa result = inventoryRepoService.findEntryByType(BeanTypeEnum.ARABICA);

        assertNull(result);
    }

    @Test
    public void Given_BeadDataExists_When_AQueryForAllBeanDataIsMade_Then_AllBeanDataShouldBeReturned() {
        Iterable<InventoryEntryJpa> mock = getMockInventoryList();
        when(inventoryRepository.findAll()).thenReturn(mock);

        List<InventoryEntryJpa> entryList = inventoryRepoService.findAllEntries();

        assertEquals(entryList.size(), 3);
    }

    private List<InventoryEntryJpa> getMockInventoryList() {
        List<InventoryEntryJpa> list = new ArrayList<InventoryEntryJpa>();
        InventoryEntryJpa mockOne = new InventoryEntryJpa();
        InventoryEntryJpa mockTwo = new InventoryEntryJpa();
        InventoryEntryJpa mockThree = new InventoryEntryJpa();

        mockOne.setBeanType(BeanTypeEnum.ARABICA);
        mockTwo.setBeanType(BeanTypeEnum.EXCELSA);
        mockThree.setBeanType(BeanTypeEnum.LIBERIAN);

        list.add(mockOne);
        list.add(mockTwo);
        list.add(mockThree);

        return list;
    }
}
