package org.nguyen.orderjava.models.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ORDERS")
public class OrderEntry {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "ORDER_ID")
    private String id;

    @Column(name = "ORDERED_BY")
    private String orderedBy;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orderEntry", orphanRemoval = true)
    List<OrderContentEntry> beans = new ArrayList<OrderContentEntry>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderedBy() {
        return orderedBy;
    }

    public void setOrderedBy(String orderedBy) {
        this.orderedBy = orderedBy;
    }

    public List<OrderContentEntry> getBeans() {
        return beans;
    }

    public void addBean(OrderContentEntry bean) {
        bean.setOrderEntry(this);
        this.beans.add(bean);
    }

    public void removeBean(OrderContentEntry bean) {
        this.beans.remove(bean);
        bean.removeOrderEntry();
    }
}
