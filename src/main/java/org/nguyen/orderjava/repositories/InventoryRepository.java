package org.nguyen.orderjava.repositories;

import org.nguyen.orderjava.models.jpa.InventoryEntry;
import org.springframework.data.repository.CrudRepository;

public interface InventoryRepository extends CrudRepository<InventoryEntry, String> {}
