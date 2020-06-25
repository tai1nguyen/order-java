
package org.nguyen.orderjava.models;

import java.util.Date;
import java.util.List;

public class OrderUpdateData {
    private List<OrderContentData> beans;
    private Date changeDate;

    public List<OrderContentData> getBeans() {
        return beans;
    }

    public void setBeans(List<OrderContentData> beans) {
        this.beans = beans;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }
}
