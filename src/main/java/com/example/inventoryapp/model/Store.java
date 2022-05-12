package com.example.inventoryapp.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Used for creating Store.
 */
public class Store extends NamedEntity{


    private String webAddress;
    private Set<Item> setItems= new HashSet<>();


    /**
     * Constructor for creating Store.
     * @param name Input String name of a Store-a.
     * @param webAddress Input String name of web address for a Store.
     * @param setItems Array of all items.
     */
    public Store(String name, String webAddress, Set<Item> setItems, long id) {
        super(name, id);
        this.webAddress = webAddress;
        this.setItems = setItems;
    }

    public Store(String name, String webAddress, Set<Item> setItems) {
        super(name);
        this.webAddress = webAddress;
        this.setItems = setItems;
    }

    public Store(String name, String webAddress, long id) {
        super(name, id);
        this.webAddress = webAddress;
    }

    public Store(String name, String webAddress) {
        super(name);
        this.webAddress = webAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Store store = (Store) o;
        return Objects.equals(webAddress, store.webAddress) && Objects.equals(setItems, store.setItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), webAddress, setItems);
    }

    public String getWebAddress() { return webAddress; }

    public void setWebAddress(String webAddress) { this.webAddress = webAddress; }

    public Set<Item> getSetItems() {
        return setItems;
    }

    public void setSetItems(Set<Item> setItems) {
        this.setItems = setItems;
    }

    @Override
    public String toString() {
        return getName() + " " +  getWebAddress();
    }
}
