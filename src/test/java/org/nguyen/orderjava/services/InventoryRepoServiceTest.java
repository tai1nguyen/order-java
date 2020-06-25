package org.nguyen.orderjava.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.nguyen.orderjava.models.BeanType;
import org.nguyen.orderjava.models.jpa.InventoryEntry;
import org.nguyen.orderjava.repositories.InventoryRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class InventoryRepoServiceTest {

    @InjectMocks
    InventoryRepoService inventoryRepoService;

    @Mock
    InventoryRepository inventoryRepository;

    @Test
    void findEntryByType_ShouldReturnBeanData_GivenBeanDataExistsInRepo() {
        InventoryEntry entry = new InventoryEntry();
        Optional<InventoryEntry> mock = null;
        
        entry.setBeanType(BeanType.ARABICA);
        entry.setPricePerUnit("0");
        entry.setWeightPerUnit("0");
        entry.setQuantity("0");

        mock = Optional.of(entry);

        when(inventoryRepository.findById(BeanType.ARABICA.getName())).thenReturn(mock);

        InventoryEntry result = inventoryRepoService.findEntryByType(BeanType.ARABICA);

        assertEquals(entry, result);
    }

    @Test
    void findEntryByType_ShouldReturnNull_GivenBeanDataDoesNotExistsInRepo() {
        Optional<InventoryEntry> mock = Optional.ofNullable(null);

        when(inventoryRepository.findById(BeanType.ARABICA.getName())).thenReturn(mock);

        InventoryEntry result = inventoryRepoService.findEntryByType(BeanType.ARABICA);

        assertNull(result);
    }

    @Test
    void findAllEntries_ShouldReturnAllEntries_GivenBeanDataExistsInRepo() {
        Iterable<InventoryEntry> mock = getMockInventoryList();

        when(inventoryRepository.findAll()).thenReturn(mock);

        List<InventoryEntry> entryList = inventoryRepoService.findAllEntries();

        assertEquals(entryList.size(), 3);
    }

    List<InventoryEntry> getMockInventoryList() {
        List<InventoryEntry> list = new ArrayList<InventoryEntry>();
        InventoryEntry mockOne = new InventoryEntry();
        InventoryEntry mockTwo = new InventoryEntry();
        InventoryEntry mockThree = new InventoryEntry();

        mockOne.setBeanType(BeanType.ARABICA);
        mockTwo.setBeanType(BeanType.EXCELSA);
        mockThree.setBeanType(BeanType.LIBERIAN);

        list.add(mockOne);
        list.add(mockTwo);
        list.add(mockThree);

        return list;
    }
}
