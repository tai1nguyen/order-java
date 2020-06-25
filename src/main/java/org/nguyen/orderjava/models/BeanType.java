package org.nguyen.orderjava.models;

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