package com.example.inventoryapp.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public record Discount(BigDecimal discountAmount) implements Serializable {


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Discount discount = (Discount) o;
        return Objects.equals(discountAmount, discount.discountAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(discountAmount);
    }
}
