package org.nguyen.orderjava.models.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.nguyen.orderjava.models.BeanTypeEnum;

public class OrderContentDto {

    private BeanTypeEnum beanType;

    private Integer quantity;

    public OrderContentDto() {}

    public OrderContentDto(BeanTypeEnum beanType, Integer quantity) {
        this.beanType = beanType;
        this.quantity = quantity;
    }

    public BeanTypeEnum getBeanType() {
        return beanType;
    }

    public void setBeanType(BeanTypeEnum beanType) {
        this.beanType = beanType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object suspect) {
        return EqualsBuilder.reflectionEquals(this, suspect);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
