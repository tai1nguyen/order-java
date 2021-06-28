package org.nguyen.orderjava.models.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.nguyen.orderjava.models.BeanTypeEnum;

@Entity
@Table(name = "ORDER_CONTENT")
public class OrderContentJpa {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(
        name = "system-uuid",
        strategy = "uuid"
    )
    @Column(name = "ID")
    private String id;

    @Column(name = "BEAN_TYPE")
    private String beanType;

    @Column(name = "QUANTITY")
    private String quantity;

    public OrderContentJpa() {}

    public OrderContentJpa(BeanTypeEnum beanType, Integer quantity) {
        this.beanType = beanType.getName();
        this.quantity = String.valueOf(quantity);
    }

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

    public Integer getQuantity() {
        return Integer.valueOf(quantity);
    }

    public void setQuantity(Integer quantity) {
        this.quantity = String.valueOf(quantity);
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
