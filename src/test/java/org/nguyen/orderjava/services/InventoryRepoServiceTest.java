package org.nguyen.orderjava.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nguyen.orderjava.models.BeanTypeEnum;
import org.nguyen.orderjava.models.jpa.InventoryEntryJpa;
import org.nguyen.orderjava.repositories.InventoryRepository;

class InventoryRepoServiceTest {

    private InventoryRepoService inventoryRepoService;

    private InventoryRepository inventoryRepository;

    @BeforeEach
    public void setUp() {
        this.inventoryRepository = mock(InventoryRepository.class);
        this.inventoryRepoService = new InventoryRepoService(this.inventoryRepository);
    }

    @Test
    void Given_BeanDataExists_When_AQueryForBeanByTypeIsMade_Then_BeanDataShouldBeReturned() {
        InventoryEntryJpa entry = new InventoryEntryJpa(
            BeanTypeEnum.ARABICA,
            new BigDecimal("0"),
            new BigDecimal("0"),
            0
        );
        Optional<InventoryEntryJpa> mock = null;
        entry.setBeanType(BeanTypeEnum.ARABICA);
        mock = Optional.of(entry);
        when(inventoryRepository.findById(BeanTypeEnum.ARABICA.getName())).thenReturn(mock);

        InventoryEntryJpa result = inventoryRepoService.findEntryByType(BeanTypeEnum.ARABICA);

        assertEquals(entry, result);
    }

    @Test
    void Given_BeanDataDoesNotExist_When_AQueryForBeanByTypeIsMade_Then_NoDataShouldBeReturned() {
        Optional<InventoryEntryJpa> mock = Optional.ofNullable(null);
        when(inventoryRepository.findById(BeanTypeEnum.ARABICA.getName())).thenReturn(mock);

        InventoryEntryJpa result = inventoryRepoService.findEntryByType(BeanTypeEnum.ARABICA);

        assertNull(result);
    }

    @Test
    void Given_BeadDataExists_When_AQueryForAllBeanDataIsMade_Then_AllBeanDataShouldBeReturned() {
        Iterable<InventoryEntryJpa> mock = getMockInventoryList();
        when(inventoryRepository.findAll()).thenReturn(mock);

        List<InventoryEntryJpa> entryList = inventoryRepoService.findAllEntries();

        assertEquals(3, entryList.size());
    }

    private List<InventoryEntryJpa> getMockInventoryList() {
        List<InventoryEntryJpa> list = new ArrayList<InventoryEntryJpa>();

        list.add(createInventoryEntry(BeanTypeEnum.ARABICA));
        list.add(createInventoryEntry(BeanTypeEnum.EXCELSA));
        list.add(createInventoryEntry(BeanTypeEnum.LIBERIAN));

        return list;
    }

    private InventoryEntryJpa createInventoryEntry(BeanTypeEnum beanType) {
        InventoryEntryJpa mock = new InventoryEntryJpa();

        mock.setBeanType(BeanTypeEnum.ARABICA);
        mock.setPricePerUnit(new BigDecimal("0"));
        mock.setWeightPerUnit(new BigDecimal("0"));
        mock.setQuantity(1);

        return mock;
    }
}
