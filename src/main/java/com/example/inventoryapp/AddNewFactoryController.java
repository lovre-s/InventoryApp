package com.example.inventoryapp;


import com.example.inventoryapp.model.Address;
import com.example.inventoryapp.model.Category;
import com.example.inventoryapp.model.Factory;
import com.example.inventoryapp.model.Item;
import database.Database;
import javafx.fxml.FXML;
import javafx.scene.control.*;


import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class AddNewFactoryController {



    List<Address> addressList = Database.getAddressesFromDataBase();

    List<Category> catList = Database.getCategoriesFromDataBase();

    List<Factory> factoryList = Database.getFactoryFromDataBase(addressList);

    List<Item> itemList = Database.getItemsFromDataBase(catList);

    public AddNewFactoryController() throws SQLException, IOException {


    }



    @FXML
    private TextField factoryNameFieldText;

    @FXML
    private ChoiceBox<String> addFactoryAddressChoiceBox;

    @FXML
    private MenuButton addFactoryMenuButton;




    public List<CheckMenuItem> MenuButtonItems(){
        List<CheckMenuItem> menuItemList = new ArrayList<>();
        for(Item item : itemList ){
            CheckMenuItem tmpItem = new CheckMenuItem(item.getName());
            menuItemList.add(tmpItem);
        }
        return menuItemList;
    }

    public List<String> choiceBoxItems(){
        List<String> choiceBoxFactory = new ArrayList<>();
        for(Address address : addressList ){
            choiceBoxFactory.add(address.getGrad());
            System.out.println(address.getGrad());
        }
        return choiceBoxFactory;
    }


    @FXML
    private void initialize() throws SQLException, IOException {

        addFactoryMenuButton.getItems().addAll(MenuButtonItems());

        addFactoryAddressChoiceBox.getItems().addAll(choiceBoxItems());

        factoryList = Database.getFactoryFromDataBase(addressList);

    }


    @FXML
    protected void onAddFactoryButtonClick() throws SQLException, IOException {

        StringBuilder errorMsg = new StringBuilder();

        String name = factoryNameFieldText.getText();
        if(name.isEmpty()){
            errorMsg.append("Name cannot be empty!\n");
        }
        String choiceString = addFactoryAddressChoiceBox.getValue();

        Address newAddress = null;
        if(choiceString != null){
            for(Address address : addressList){
                if(choiceString.equals(address.getGrad())){
                    newAddress = new Address(address.getStreet(),address.getHouseNumber(),
                            address.getGrad(),address.getPostalCode(),address.getId());
                }
            }
        }

        if(choiceString == null){
            errorMsg.append("Address must be selected!\n");
        }

        List<MenuItem> tmpItemList = addFactoryMenuButton.getItems();

        List<Item> tmpFactoryItems = new ArrayList<>();
        Integer counter = 0;
        for(MenuItem menuItem : tmpItemList){
            if(((CheckMenuItem)menuItem).isSelected()){
                for(Item item : itemList){
                    if(menuItem.getText().equals(item.getName())){
                        tmpFactoryItems.add(new Item(item.getName(),item.getCategory(),item.getWidth(),
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

            Set<Item> tmpItems = new HashSet<>(tmpFactoryItems);

            Factory newFactory = new Factory(name,newAddress,tmpItems);

            Database.insertFactoryToDatabase(newFactory);


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save successful!");
            alert.setHeaderText("Factory data saved!");
            alert.setContentText("Factory: " + name + " saved to the database!");

            alert.showAndWait();


        }
    else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save failed!");
            alert.setHeaderText("Factory data not saved!");
            alert.setContentText(errorMsg.toString());

            alert.showAndWait();

    }

    }


    }
