package org.nguyen.orderjava.models.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.nguyen.orderjava.models.BeanTypeEnum;

@Entity
@Table(name = "ORDERS")
public class OrderJpa {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "ORDER_ID")
    private String id;

    @Column(name = "ORDERED_BY")
    private String orderedBy;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    List<OrderContentJpa> contents = new ArrayList<>();

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

    public List<OrderContentJpa> getContents() {
        return contents;
    }

    public void addContent(OrderContentJpa content) {
        this.contents.add(content);
    }

    public void removeContent(BeanTypeEnum type) {
        OrderContentJpa content = findMatchingContent(
            type.getName(), this.contents
        );

        this.contents.remove(content);
    }

    @Override
    public boolean equals(Object suspect) {
        return EqualsBuilder.reflectionEquals(this, suspect);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    private OrderContentJpa findMatchingContent(String type, List<OrderContentJpa> suspectContent) {
        for (OrderContentJpa suspect : suspectContent) {
            if (suspect.getBeanType().equals(type)) {
                return suspect;
            }
        }

        return null;
    }
}
