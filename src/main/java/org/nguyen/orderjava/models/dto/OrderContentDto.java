package org.nguyen.orderjava.models.dto;

import org.nguyen.orderjava.models.BeanTypeEnum;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderContentDto {

    private BeanTypeEnum beanType;

    private Integer quantity;

    public OrderContentDto(BeanTypeEnum beanType, Integer quantity) {
        this.beanType = beanType;
        this.quantity = quantity;
    }
}
