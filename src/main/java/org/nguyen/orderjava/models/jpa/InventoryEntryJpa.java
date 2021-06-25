package org.nguyen.orderjava.models.jpa;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.nguyen.orderjava.models.BeanTypeEnum;

@Entity
@Table(name = "INVENTORY")
public class InventoryEntryJpa {

    @Id
    @Column(name = "BEAN_TYPE")
    private String beanType;

    @Column(name = "WEIGHT_PER_UNIT")
    private String weightPerUnit;

    @Column(name = "PRICE_PER_UNIT")
    private String pricePerUnit;

    @Column(name = "QUANTITY")
    private String quantity;

    public InventoryEntryJpa() {}

    public InventoryEntryJpa(
            BeanTypeEnum beanType,
            BigDecimal weightPerUnit,
            BigDecimal pricePerUnit,
            Integer quantity
    ) {
        this.beanType = beanType.getName();
        this.weightPerUnit = weightPerUnit.toPlainString();
        this.pricePerUnit = pricePerUnit.toPlainString();
        this.quantity = String.valueOf(quantity);
    }

    public BeanTypeEnum getBeanType() {
        return BeanTypeEnum.getType(beanType);
    }

    public void setBeanType(BeanTypeEnum beanType) {
        this.beanType = beanType.getName();
    }

    public BigDecimal getWeightPerUnit() {
        return new BigDecimal(weightPerUnit);
    }

    public void setWeightPerUnit(BigDecimal weightPerUnit) {
        this.weightPerUnit = weightPerUnit.toPlainString();
    }

    public BigDecimal getPricePerUnit() {
        return new BigDecimal(pricePerUnit);
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit.toPlainString();
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
