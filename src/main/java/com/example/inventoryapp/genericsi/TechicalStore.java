package com.example.inventoryapp.genericsi;

import com.example.inventoryapp.model.Item;
import com.example.inventoryapp.model.Store;
import com.example.inventoryapp.model.Technical;

import java.util.List;
import java.util.Set;

public class TechicalStore<T extends Technical> extends Store {

    private List<T> listTechItems;


    public TechicalStore(String name, String webAddress, List<T> listFoodItems, Set<Item> setItems, long id) {
        super(name, webAddress, setItems, id);
        this.listTechItems = listFoodItems;
    }

    @Override
    public Set<Item> getSetItems() {
        return super.getSetItems();
    }



    public List<T> getListTechItems() { return listTechItems; }

    public void setListTechItems(List<T> listTechItems) { this.listTechItems = listTechItems; }

}
