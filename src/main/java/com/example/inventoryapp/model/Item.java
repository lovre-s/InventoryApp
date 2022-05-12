package com.example.inventoryapp.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Used for creating Item.
 */

public class Item extends NamedEntity{



    private Category category;

    long catId;
    BigDecimal width;
    BigDecimal height;
    BigDecimal length;
    BigDecimal productionCost;
    BigDecimal sellingPrice;
    Discount discount;

    /**
     * Constructor for creating an Item.
     * @param name Input String name of an Item.
     * @param category Input object Category to Item.
     * @param width Input Bigdecimal number of Item width.
     * @param height Input Bigdecimal number of Item height.
     * @param length Input Bigdecimal number of Item length.
     * @param productionCost Input Bigdecimal number of Item production cost.
     * @param sellingPrice Input Bigdecimal number of Item selling price.
     * @param discount Input Discount object, discount amount for an Item.
     */

    public Item(String name, Category category, BigDecimal width, BigDecimal height,
                BigDecimal length, BigDecimal productionCost, BigDecimal sellingPrice, Discount discount, long id) {
        super(name, id);
        this.category = category;
        this.width = width;
        this.height = height;
        this.length = length;
        this.productionCost = productionCost;
        this.sellingPrice = sellingPrice;
        this.discount = discount;
    }

    public Item(String name, Category category, BigDecimal width, BigDecimal height,
                BigDecimal length, BigDecimal productionCost, BigDecimal sellingPrice,long id) {
        super(name, id);
        this.category = category;
        this.width = width;
        this.height = height;
        this.length = length;
        this.productionCost = productionCost;
        this.sellingPrice = sellingPrice;
    }

    public Item(String name, Category category, BigDecimal width, BigDecimal height,
                BigDecimal length, BigDecimal productionCost, BigDecimal sellingPrice) {
        super(name);
        this.category = category;
        this.width = width;
        this.height = height;
        this.length = length;
        this.productionCost = productionCost;
        this.sellingPrice = sellingPrice;

    }

    public Item(long catId,String name,Category category, BigDecimal width, BigDecimal height,
                BigDecimal length, BigDecimal productionCost, BigDecimal sellingPrice) {
        super(name);
        this.catId = catId;
        this.category = category;
        this.width = width;
        this.height = height;
        this.length = length;
        this.productionCost = productionCost;
        this.sellingPrice = sellingPrice;
    }

    public Item(long catId,String name, BigDecimal width, BigDecimal height,
                BigDecimal length, BigDecimal productionCost, BigDecimal sellingPrice) {
        super(name);
        this.catId = catId;
        this.width = width;
        this.height = height;
        this.length = length;
        this.productionCost = productionCost;
        this.sellingPrice = sellingPrice;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Item item = (Item) o;
        return Objects.equals(category, item.category) && Objects.equals(width, item.width) &&
                Objects.equals(height, item.height) && Objects.equals(length, item.length) &&
                Objects.equals(productionCost, item.productionCost) &&
                Objects.equals(sellingPrice, item.sellingPrice) && Objects.equals(discount, item.discount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), category, width, height, length, productionCost, sellingPrice, discount);
    }

    public Category getCategory() { return category; }

    public void setCategory(Category category) { this.category = category; }

    public BigDecimal getWidth() { return width; }

    public void setWidth(BigDecimal width) { this.width = width; }

    public BigDecimal getHeight() { return height; }

    public void setHeight(BigDecimal height) { this.height = height; }

    public BigDecimal getLength() { return length; }

    public void setLength(BigDecimal length) { this.length = length; }

    public BigDecimal getProductionCost() { return productionCost; }

    public void setProductionCost(BigDecimal productionCost) { this.productionCost = productionCost; }

    public BigDecimal getSellingPrice() { return sellingPrice; }

    public void setSellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; }

    public Discount getDiscount() { return discount; }

    public void setDiscount(Discount discount) { this.discount = discount; }


    @Override
    public String toString() {
        return  getName()
                ;
    }
}
