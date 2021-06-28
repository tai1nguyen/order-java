package org.nguyen.orderjava.models.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDto {

    private List<OrderContentDto> contents;

    private BigDecimal price = new BigDecimal(0);

    private Date orderDate;

    private Date deliveryDate;

    private boolean isComplete;

    private String orderedBy;

    private String id;
}
