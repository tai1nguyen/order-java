package org.nguyen.orderjava.models;

public enum BeanTypeEnum {

    ARABICA("ARABICA"),
    ROBUSTA("ROBUSTA"),
    LIBERIAN("LIBERIAN"),
    EXCELSA("EXCELSA");

    private final String name;

    BeanTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static BeanTypeEnum getType(String type) {
        for (BeanTypeEnum beanType : BeanTypeEnum.values()) {
            if (beanType.getName().equalsIgnoreCase(type)) {
                return beanType;
            }
        }

        return null;
    }
}
