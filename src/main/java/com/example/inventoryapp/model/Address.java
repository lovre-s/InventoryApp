package com.example.inventoryapp.model;

import com.example.inventoryapp.enumls.City;

import java.io.Serializable;
import java.util.Objects;

/**
 * Used for creating Addresses.
 */

public class Address implements Serializable {



    private long id;
    private String street;
    private String houseNumber;
    private City city;
    private String grad;
    private String postalCode;


    public Address(String street, String houseNumber, String grad, String postalCode, long id) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.grad = grad;
        this.postalCode = postalCode;
        this.id = id;
    }


    /**
     * Builder class for inputting addresses.
     */

    public static class Builder{

        private String street;
        private String houseNumber;
        private City city;

        public Builder(String street){
            this.street = street;
        }

        public Builder atHouseNumber(String houseNumber){
            this.houseNumber = houseNumber;
            return this;
        }

        public Builder inCity(City city){
            this.city = city;
            return this;
        }

        public Address build(){
            Address street = new Address();
            street.street = this.street;
            street.houseNumber = this.houseNumber;
            street.city = this.city;

            return street;
        }

    }
    private Address(){
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street) && Objects.equals(houseNumber, address.houseNumber)
                && city == address.city;
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, houseNumber, city);
    }

    public String getStreet() { return street; }

    public String getHouseNumber() { return houseNumber; }

    public City getCity() { return city; }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        return  street + " " + houseNumber + " " + grad + " " + postalCode;
    }
}

