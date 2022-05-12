package com.example.inventoryapp.genericsi;

import com.example.inventoryapp.model.Edible;
import com.example.inventoryapp.model.Item;
import com.example.inventoryapp.model.Store;

import java.util.List;
import java.util.Set;


public class FoodStore  <T extends Edible> extends Store {

    private List<T> listFoodItems;

    public FoodStore(String name, String webAddress, List<T> listFoodItems, Set<Item> setItems, long id) {
        super(name, webAddress, setItems, id);
        this.listFoodItems = listFoodItems;
    }

    public List<T> getListFoodItems() { return listFoodItems; }

    public void setListFoodItems(List<T> listFoodItems) { this.listFoodItems = listFoodItems; }


}
