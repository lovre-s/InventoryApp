package com.example.inventoryapp;



import com.example.inventoryapp.model.Category;
import com.example.inventoryapp.model.Item;
import com.example.inventoryapp.model.Store;
import database.Database;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class AddNewStoreController {


    List<Category> catList = Database.getCategoriesFromDataBase();
    List<Item> itemList = Database.getItemsFromDataBase(catList);
    List<Store> storeList = Database.getStoreFromDataBase();

    public AddNewStoreController() throws SQLException, IOException {
    }

    @FXML
    private TextField storeNameFieldText;

    @FXML
    private TextField addStoreWebAddressFieldText;

    @FXML
    private MenuButton addStoreMenuButton;

    public List<CheckMenuItem> MenuButtonItems(){
        List<CheckMenuItem> menuItemList = new ArrayList<>();
        for(Item item : itemList ){
            CheckMenuItem tmpItem = new CheckMenuItem(item.getName());
            menuItemList.add(tmpItem);
        }
        return menuItemList;
    }

    @FXML
    private void initialize() throws SQLException, IOException {

        addStoreMenuButton.getItems().addAll(MenuButtonItems());

        storeList = Database.getStoreFromDataBase();

    }

    @FXML
    protected void onAddStoreButtonClick() throws SQLException, IOException {

        StringBuilder errorMsg = new StringBuilder();

        String name = storeNameFieldText.getText();
        if(name.isEmpty()){
            errorMsg.append("Name cannot be empty!\n");
        }
        String webAddress = addStoreWebAddressFieldText.getText();
        if(webAddress.isEmpty()){
            errorMsg.append("Web Address cannot be empty!\n");
        }

        List<MenuItem> tmpItemList = addStoreMenuButton.getItems();

        Integer counter = 0;
        List<Item> tmpStoreList = new ArrayList<>();
        for(MenuItem menuItem : tmpItemList){
            if(((CheckMenuItem)menuItem).isSelected()){
                for(Item item : itemList){
                    if(menuItem.getText().equals(item.getName())){
                        tmpStoreList.add(new Item(item.getName(),item.getCategory(),item.getWidth(),
                                item.getHeight(),item.getLength(),item.getProductionCost(),item.getSellingPrice(),item.getId()));
                    }
                }
                counter++;
            }
        }
        if(counter == 0) {
            errorMsg.append("Items must be selected!\n");
        }

        if(errorMsg.isEmpty()){

            Set<Item> tmpItems = new HashSet<>(tmpStoreList);

            Store newStore = new Store(name,webAddress,tmpItems);

            Database.insertStoreToDatabase(newStore);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save successful!");
            alert.setHeaderText("Store data saved!");
            alert.setContentText("Store: " + name + " saved to the database!");

            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save failed!");
            alert.setHeaderText("Store data not saved!");
            alert.setContentText(errorMsg.toString());

            alert.showAndWait();
        }
    }

}
