package org.nguyen.orderjava.models.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ORDER_CONTENT")
public class OrderContentEntry {

    @Id
    @GeneratedValue
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
}
