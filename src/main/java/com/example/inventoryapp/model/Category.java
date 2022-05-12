package com.example.inventoryapp.model;

import java.util.Objects;

/**
 * Used for creating Categories.
 */

public class Category extends NamedEntity{



    private String description;

    /**
     * Constructor for creating Categories.
     * @param name String name of a Category.
     * @param description String description of a Category.
     */

    public Category(String name, String description, long id) {
        super(name, id);
        this.description = description;
    }

    public Category(String name, String description) {
        super(name);
        this.description = description;
    }

    public Category(String name) {
        super(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Category category = (Category) o;
        return Objects.equals(description, category.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), description);
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }


    @Override
    public String toString() {
        return  getName() + " "
                + description;
    }
}
