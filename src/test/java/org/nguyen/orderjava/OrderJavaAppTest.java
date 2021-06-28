package org.nguyen.orderjava;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class OrderJavaAppTest {
    
    @Test
    void When_MainMethodIsExecuted_Then_ContextShouldLoad() {
        assertDoesNotThrow(() -> {
            OrderJavaApp.main(new String[] {});
        });
    }
}
