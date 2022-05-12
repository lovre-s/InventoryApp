package com.example.inventoryapp.enumls;

public enum City {

    ZADAR ("Zadar", "23000"),
    ZAGREB ("Zagreb", "10000"),
    MILANO ("Milano","20151");

    private String postalCode;
    private String cityName;


    City(String cityName, String postalCode) {
        this.postalCode = postalCode;
        this.cityName = cityName;
    }

    public String getPostalCode() { return postalCode; }

    public String getCityName() { return cityName; }

}
