package org.nguyen.orderjava.models;

public class OrderContentData {
    private BeanType beanType;
    private Integer quantity;

    public BeanType getBeanType() {
        return beanType;
    }

    public void setBeanType(BeanType beanType) {
        this.beanType = beanType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof OrderContentData) {
            OrderContentData suspect = (OrderContentData) o;
            return isEveryPropertyEqual(suspect);
        }
        else {
            return false;
        }
    }

    private boolean isEveryPropertyEqual(OrderContentData suspect) {
        return isEqual(this.beanType, suspect.getBeanType()) &&
            isEqual(this.quantity, suspect.getQuantity());
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