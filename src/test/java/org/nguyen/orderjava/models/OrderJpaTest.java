package org.nguyen.orderjava.models;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.nguyen.orderjava.models.jpa.OrderContentJpa;
import org.nguyen.orderjava.models.jpa.OrderJpa;

class OrderJpaTest {
    
    @Test
    void Given_OrderContentDoesNotExistOnOrder_When_TryingToRemoveOrderContent_Then_NothingShouldHappen() {
        OrderJpa order = new OrderJpa();
        OrderContentJpa orderContent = new OrderContentJpa();
        orderContent.setBeanType(BeanTypeEnum.ARABICA.getName());
        order.addContent(orderContent);

        assertDoesNotThrow(() -> {
            order.removeContent(BeanTypeEnum.EXCELSA);
        });
    }

    @Test
    void Given_OrderContentExistsOnOrder_When_TryingToRemoveOrderContent_Then_ContentShouldBeRemoved() {
        OrderJpa order = new OrderJpa();
        OrderContentJpa orderContent = new OrderContentJpa();
        orderContent.setBeanType(BeanTypeEnum.ARABICA.getName());
        order.addContent(orderContent);

        order.removeContent(BeanTypeEnum.ARABICA);

        assertEquals(0, order.getContents().size());
    }
}
