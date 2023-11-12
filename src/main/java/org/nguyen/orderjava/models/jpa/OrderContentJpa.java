package org.nguyen.orderjava.models.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.nguyen.orderjava.models.BeanTypeEnum;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "ORDER_CONTENT")
public class OrderContentJpa {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "ID")
    private String id;

    @Column(name = "BEAN_TYPE")
    private String beanType;

    @Column(name = "QUANTITY")
    private String quantity;

    public OrderContentJpa(BeanTypeEnum beanType, Integer quantity) {
        this.beanType = beanType.getName();
        this.quantity = String.valueOf(quantity);
    }

    public Integer getQuantity() {
        return Integer.valueOf(quantity);
    }

    public void setQuantity(Integer quantity) {
        this.quantity = String.valueOf(quantity);
    }
}
