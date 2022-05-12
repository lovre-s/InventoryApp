package com.example.inventoryapp.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Used for creating Pear Item,
 * and implements interface Edible.
 */

public class Pear extends Item implements Edible{

    public static final int CALORIES_PER_KILOGRAM = 780;

    BigDecimal weight;

    /**
     * Constructor for creating a Pear item.
     * @param name Input String name of an Item.
     * @param category Input object Category to Item.
     * @param width Input Bigdecimal number of Item width.
     * @param height Input Bigdecimal number of Item height.
     * @param length Input Bigdecimal number of Item length.
     * @param productionCost Input Bigdecimal number of Item production cost.
     * @param sellingPrice Input Bigdecimal number of Item selling price.
     * @param weight Input BigDecimal number, mass in kilograms of an Item.
     * @param discount Input Discount object, discount amount for an Item.
     */

    public Pear(String name, Category category, BigDecimal width, BigDecimal height,
                BigDecimal length, BigDecimal productionCost, BigDecimal sellingPrice,
                BigDecimal weight, Discount discount, long id) {
        super(name, category, width, height, length, productionCost, sellingPrice, discount, id);
        this.weight = weight;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Pear zeusFaber = (Pear) o;
        return Objects.equals(weight, zeusFaber.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), weight);
    }

    public BigDecimal getWeight() { return weight; }

    public void setWeight(BigDecimal weight) { this.weight = weight; }

    /**
     * Used for calculating kilocalories
     * @return Correctly parsed whole number.
     */

    @Override
    public int calculateKilocalories() {

        return weight.multiply(BigDecimal.valueOf(CALORIES_PER_KILOGRAM)).intValue();
    }

    /**
     * Used for calculating price per kilogram
     * @return Correctly parsed BigDecimal number.
     */

    @Override
    public BigDecimal calculatePrice() {

        BigDecimal discountPrice;
        BigDecimal tmpDiscount;

        tmpDiscount = (sellingPrice.multiply(weight).multiply(discount.discountAmount()))
                .divide(BigDecimal.valueOf(100));
        discountPrice = sellingPrice.multiply(weight).subtract(tmpDiscount);

        return discountPrice;
    }
}

