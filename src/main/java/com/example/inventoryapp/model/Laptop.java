package com.example.inventoryapp.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Used for creating Laptop, extends class Item and implements interface "Technical".
 */
public final class Laptop extends Item implements Technical {


    int warranty;

    /**
     * Constructor for creating a Laptop.
     * @param name Input String name of an Item.
     * @param category Input object Category to Item.
     * @param width Input Bigdecimal number of Item width.
     * @param height Input Bigdecimal number of Item height.
     * @param length Input Bigdecimal number of Item length.
     * @param productionCost Input Bigdecimal number of Item production cost.
     * @param sellingPrice Input Bigdecimal number of Item selling price.
     * @param warranty Input whole number, determing length of warranty in months.
     */

    public Laptop(String name, Category category, BigDecimal width, BigDecimal height,
                  BigDecimal length, BigDecimal productionCost, BigDecimal sellingPrice, Discount discount,
                  int warranty, long id) {
        super(name, category, width, height, length, productionCost, sellingPrice, discount, id);
        this.warranty = warranty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Laptop laptop = (Laptop) o;
        return warranty == laptop.warranty;
    }


    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), warranty);
    }

    /**
     * Used for inputting whole number, length of warranty in months.
     * @return Correctly parsed whole number.
     */
    @Override
    public int expiryDateAfter() {



        return warranty;
    }


}
