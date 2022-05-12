package com.example.inventoryapp.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Used for creating Factories.
 */


public class Factory extends NamedEntity{



    private Address address;
    private Set<Item> setItems= new HashSet<>();
    private long addressId;

    /**
     * Constructor for creating Factory.
     * @param name Input String name of a Factory.
     * @param address Inputting address to a Factory.
     * @param setItems Set of all items.
     */

    public Factory(String name, Address address, Set<Item> setItems, long id) {
        super(name, id);
        this.address = address;
        this.setItems = setItems;
    }

    public Factory(String name, long id, Address address,Set<Item> setItems) {
        super(name, id);
        this.address = address;
        this.setItems = setItems;
    }

    public Factory(String name, long id, Address address) {
        super(name, id);
        this.address = address;
    }

    public Factory(String name, Address address, Set<Item> setItems) {
        super(name);
        this.address = address;
        this.setItems = setItems;
    }

    public Factory(String name, Address address, long addressId) {
        super(name);
        this.address = address;
        this.addressId = addressId;
    }

/*    public Factory(String name, Address address, long addressId) {
        super(name);
        this.address = address;
        this.addressId = addressId;
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Factory factory = (Factory) o;
        return Objects.equals(address, factory.address) && Objects.equals(setItems, factory.setItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), address, setItems);
    }

    public Address getAddress() { return address; }

    public void setAddress(Address address) { this.address = address; }

    public Set<Item> getSetItems() {
        return setItems;
    }

    public void setSetItems(Set<Item> setItems) {
        this.setItems = setItems;
    }


    @Override
    public String toString() {
        return getName();
    }
}
