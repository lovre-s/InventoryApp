package com.example.inventoryapp.model;

import java.math.BigDecimal;

/**
 * Used to determine if items are Edible and calculating price per kilogram and kilcalories in an item.
 */

public interface Edible {

    /**
     * Used for calculating kilocalories
     * @return Correctly parsed whole number.
     */
    public int calculateKilocalories();

    /**
     * Used for calculating price per kilogram
     * @return Correctly parsed BigDecimal number.
     */

    public BigDecimal calculatePrice();



}
