package org.nguyen.orderjava.models.jpa;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.nguyen.orderjava.models.BeanTypeEnum;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
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
}
