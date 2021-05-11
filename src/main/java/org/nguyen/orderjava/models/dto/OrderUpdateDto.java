
package org.nguyen.orderjava.models.dto;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class OrderUpdateDto {

    private String id;

    private List<OrderContentDto> beanAdditions;

    private List<OrderContentDto> beanDeletions;

    private List<OrderContentDto> beanUpdates;

    private Date changeDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<OrderContentDto> getBeanAdditions() {
        return beanAdditions;
    }

    public void setBeanAdditions(List<OrderContentDto> beanAdditions) {
        this.beanAdditions = beanAdditions;
    }

    public List<OrderContentDto> getBeanDeletions() {
        return beanDeletions;
    }

    public void setBeanDeletions(List<OrderContentDto> beanDeletions) {
        this.beanDeletions = beanDeletions;
    }

    public List<OrderContentDto> getBeanUpdates() {
        return beanUpdates;
    }

    public void setBeanUpdates(List<OrderContentDto> beanUpdates) {
        this.beanUpdates = beanUpdates;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    @Override
    public boolean equals(Object suspect) {
        return EqualsBuilder.reflectionEquals(this, suspect);
    }
}
