package com.example.inventoryapp.model;

/**
 * Used for determining which Item belongs to Technical.
 */
public sealed interface Technical permits Laptop{

    /**
     * Used to calculate length of expiry date.
     * @return Correctly parsed whole number which determents length of an expiration date of a Laptop.
     */
    int expiryDateAfter();

}