package org.nguyen.orderjava.models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.nguyen.orderjava.models.Bean.BeanType;

public class OrderData {
    private List<Bean> beans;
    private BigDecimal price = new BigDecimal(0);
    private Date orderDate;
    private Date deliveryDate;
    private boolean isComplete;
    private String orderedBy;
    private String id;

    public List<Bean> getBeans() {
        return beans;
    }

    public void setBeans(List<Bean> beans) {
        this.beans = beans;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliverDate) {
        this.deliveryDate = deliverDate;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public String getOrderedBy() {
        return orderedBy;
    }

    public void setOrderedBy(String orderedBy) {
        this.orderedBy = orderedBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof OrderData) {
            OrderData suspect = (OrderData) o;
            return isEveryPropertyEqual(suspect) &&
                isContentEqual(suspect.getBeans());
        }
        else {
            return false;
        }
    }

    private boolean isEveryPropertyEqual(OrderData suspect) {
        return isEqual(this.id, suspect.getId()) &&
            isEqual(this.deliveryDate, suspect.getDeliveryDate()) &&
            isEqual(this.orderedBy, suspect.getOrderedBy()) &&
            isEqual(this.orderDate, suspect.getOrderDate()) &&
            isEqual(this.isComplete, suspect.isComplete()) &&
            isEqual(this.price, suspect.getPrice());
            
    }

    private boolean isContentEqual(List<Bean> suspectContent) {
        boolean isEqual = true;

        for (Bean bean : this.beans) {
            Bean match = findMatchingBean(bean.getType(), suspectContent);

            if (match == null || !bean.equals(match)) {
                // The suspect does not have a matching
                // bean entry. It is therefore not equal.
                isEqual = false;
                break;
            }
        }

        return isEqual;
    }

    private Bean findMatchingBean(BeanType type, List<Bean> suspectContent) {
        for (Bean bean : suspectContent) {
            if (type.equals(bean.getType())) {
                return bean;
            }
        }

        return null;
    }

    private boolean isEqual(Object expected, Object suspect) {
        if (expected != null) {
            return expected.equals(suspect);
        }
        else {
            return expected == suspect;
        }
    }
}
