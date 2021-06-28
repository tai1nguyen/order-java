package org.nguyen.orderjava.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class BeanTypeEnumTest {

    @Test
    void Given_BeanTypeExists_When_AskedToGetBeanTypeByString_Then_BeanTypeEnumShouldBeReturned() {
        assertEquals(BeanTypeEnum.ARABICA, BeanTypeEnum.getType("arabica"));
    }

    @Test
    void Given_BeanTypeDoesNotExist_When_AskedToGetBeanTypeByString_Then_BeanTypeEnumShouldBeNull() {
        assertNull(BeanTypeEnum.getType("Not_A_Bean"));
    }
}
