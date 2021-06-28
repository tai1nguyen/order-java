
package org.nguyen.orderjava.models.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderUpdateDto {

    private String id;

    private List<OrderContentDto> contentAdditions;

    private List<OrderContentDto> contentDeletions;

    private List<OrderContentDto> contentUpdates;

    private Date changeDate;
}
