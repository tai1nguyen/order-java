
package org.nguyen.orderjava.models.dto;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class OrderUpdateDto {

    private String id;

    private List<OrderContentDto> contentAdditions;

    private List<OrderContentDto> contentDeletions;

    private List<OrderContentDto> contentUpdates;

    private Date changeDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<OrderContentDto> getContentAdditions() {
        return contentAdditions;
    }

    public void setContentAdditions(List<OrderContentDto> contentAdditions) {
        this.contentAdditions = contentAdditions;
    }

    public List<OrderContentDto> getContentDeletions() {
        return contentDeletions;
    }

    public void setContentDeletions(List<OrderContentDto> contentDeletions) {
        this.contentDeletions = contentDeletions;
    }

    public List<OrderContentDto> getContentUpdates() {
        return contentUpdates;
    }

    public void setContentUpdates(List<OrderContentDto> contentUpdates) {
        this.contentUpdates = contentUpdates;
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

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
