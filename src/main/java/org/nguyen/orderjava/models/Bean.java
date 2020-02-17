package org.nguyen.orderjava.models;

import java.math.BigDecimal;

public class Bean {
    private BigDecimal weightPerUnit = new BigDecimal(0);
    private BigDecimal pricePerUnit = new BigDecimal(0);
    private Bean.BeanType type;
    private Integer units;

    public enum BeanType {
        ARABICA("ARABICA"),
        ROBUSTA("ROBUSTA"),
        LIBERIAN("LIBERIAN"),
        EXCELSA("EXCELSA");

        private final String name;

        BeanType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static BeanType getType(String type) {
            for (BeanType beanType : BeanType.values()) {
                if (beanType.getName().equalsIgnoreCase(type)) {
                    return beanType;
                } 
            }

            return null;
        }
    }

    public BigDecimal getWeightPerUnit() {
        return weightPerUnit;
    }

    public void setWeightPerUnit(BigDecimal weightPerUnit) {
        this.weightPerUnit = weightPerUnit;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public Bean.BeanType getType() {
        return type;
    }

    public void setType(Bean.BeanType type) {
        this.type = type;
    }

    public Integer getUnits() {
        return units;
    }

    public void setUnits(Integer units) {
        this.units = units;
    }
}
