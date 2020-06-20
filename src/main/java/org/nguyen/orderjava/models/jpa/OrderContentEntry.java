package org.nguyen.orderjava.models.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ORDER_CONTENT")
public class OrderContentEntry {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "ID")
    private String id;

    @Column(name = "BEAN_TYPE")
    private String beanType;

    @Column(name = "QUANTITY")
    private String quantity;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private OrderEntry orderEntry;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBeanType() {
        return beanType;
    }

    public void setBeanType(String beanType) {
        this.beanType = beanType;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public OrderEntry getOrderEntry() {
        return orderEntry;
    }

    public void setOrderEntry(OrderEntry orderEntry) {
        this.orderEntry = orderEntry;
    }

    public void removeOrderEntry() {
        this.orderEntry = null;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof OrderContentEntry) {
            OrderContentEntry suspect = (OrderContentEntry) o;
            return isEveryPropertyEqual(suspect);
        }
        else {
            return false;
        }
    }

    private boolean isEveryPropertyEqual(OrderContentEntry suspect) {
        return isEqual(this.id, suspect.getId()) &&
            isEqual(this.beanType, suspect.getBeanType()) &&
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
