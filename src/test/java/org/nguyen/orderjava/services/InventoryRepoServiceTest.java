package org.nguyen.orderjava.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.nguyen.orderjava.models.Bean.BeanType;
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
    void getEntryByType_ShouldReturnBeanData_GivenBeanDataExistsInRepo() {
        InventoryEntry entry = new InventoryEntry();
        Optional<InventoryEntry> mock = null;
        
        entry.setBeanType(BeanType.ARABICA);
        entry.setPricePerUnit("0");
        entry.setWeightPerUnit("0");
        entry.setQuantity("0");

        mock = Optional.of(entry);

        when(inventoryRepository.findById(BeanType.ARABICA.getName())).thenReturn(mock);

        InventoryEntry result = inventoryRepoService.getEntryByType(BeanType.ARABICA);

        assertNotNull(result);
        assertEquals(entry.getBeanType(), BeanType.ARABICA);
        assertEquals(entry.getPricePerUnit(), "0");
        assertEquals(entry.getWeightPerUnit(), "0");
        assertEquals(entry.getQuantity(), "0");
    }

    @Test
    void getEntryByType_ShouldReturnNull_GivenBeanDataDoesNotExistsInRepo() {
        Optional<InventoryEntry> mock = Optional.ofNullable(null);

        when(inventoryRepository.findById(BeanType.ARABICA.getName())).thenReturn(mock);

        InventoryEntry result = inventoryRepoService.getEntryByType(BeanType.ARABICA);

        assertNull(result);
    }

    @Test
    void getAllEntries_ShouldReturnAllEntries_GivenBeanDataExistsInRepo() {

    }

    @Test
    void getAllEntries_ShouldNotReturnEntries_GivenBeadDataDoesNotExistInRepo() {
        
    }
}
