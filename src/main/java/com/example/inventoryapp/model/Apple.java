package com.example.inventoryapp.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Used for creating Apple Item, extends class Item,
 * and implements interface Edible.
 */

public class Apple extends Item implements Edible{

    public static final int CALORIES_PER_KILOGRAM = 2000;

    BigDecimal weight;

    /**
     * Constructor for creating an Apple item.
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

    public Apple(String name, Category category, BigDecimal width, BigDecimal height, BigDecimal length,
                 BigDecimal productionCost, BigDecimal sellingPrice, BigDecimal weight, Discount discount, long id) {
        super(name, category, width, height, length, productionCost, sellingPrice,discount, id);
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Apple that = (Apple) o;
        return Objects.equals(weight, that.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), weight);
    }

    public BigDecimal getWeight() { return weight; }

    public void setWeight(BigDecimal weight) { this.weight = weight; }

    /**
     * Used for calculating price per kilogram
     * @return Correctly parsed BigDecimal number.
     */
    @Override
    public  BigDecimal calculatePrice() {

        BigDecimal discountPrice;
        BigDecimal tmpDiscount;

        tmpDiscount = (sellingPrice.multiply(weight).multiply(discount.discountAmount()))
                .divide(BigDecimal.valueOf(100));
        discountPrice = sellingPrice.multiply(weight).subtract(tmpDiscount);

        return discountPrice;
    }


    /**
     * Used for calculating kilocalories
     * @return Correctly parsed whole number.
     */
    @Override
    public int calculateKilocalories() {


        return weight.multiply(BigDecimal.valueOf(CALORIES_PER_KILOGRAM)).intValue();
    }


}
