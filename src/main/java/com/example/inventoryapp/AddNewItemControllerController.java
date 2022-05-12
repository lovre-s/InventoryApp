package com.example.inventoryapp;

import com.example.inventoryapp.model.Category;
import com.example.inventoryapp.model.Item;

import database.Database;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;


import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class AddNewItemControllerController {

    List<Category> catList = Database.getCategoriesFromDataBase();
    List<Item> listaItems = Database.getItemsFromDataBase(catList);


    public AddNewItemControllerController() throws SQLException, IOException {


    }


    @FXML
    private TextField itemNameTextField;

    @FXML
    private ChoiceBox<String> itemCatChoiceBox;

    @FXML
    private TextField itemWidthTextField;

    @FXML
    private TextField itemHeightTextField;

    @FXML
    private TextField itemLengthTextField;

    @FXML
    private TextField itemProductionCostTextField;

    @FXML
    private TextField itemSellingPriceTextField;


    public List<String> choiceBoxItems(){
        List<String> choiceBoxItem = new ArrayList<>();
        for(Category category : catList ){
            choiceBoxItem.add(category.getName());
        }
        return choiceBoxItem;
    }


    @FXML
    private void initialize() throws SQLException, IOException {

        itemCatChoiceBox.getItems().addAll(choiceBoxItems());


        Database.getItemsFromDataBase(catList);



    }

    @FXML
    protected void onSaveItemButton() throws SQLException, IOException {

        StringBuilder errorMsg = new StringBuilder();

        String name = itemNameTextField.getText();

        Long catId = 0L;

        if(name.isEmpty()){
            errorMsg.append("Name cannot be empty!\n");
        }

        String choiceString = itemCatChoiceBox.getValue();
        if(choiceString != null){
            Category choice = new Category(choiceString);
            for(Category category : catList){
                if(choiceString.equals(category.getName())){
                    catId = category.getId();
                }
            }
        }
        if(choiceString == null){
            errorMsg.append("Category must be selected!\n");
        }



        BigDecimal width = BigDecimal.ZERO;
        String widthString = itemWidthTextField.getText();
        if(widthString.isEmpty()) {
            errorMsg.append("Width cannot be empty!\n");
        }
        else {
            try {
                width = new BigDecimal(widthString);
            }
            catch (NumberFormatException ex) {
                errorMsg.append("Width is not in valid format!\n");
            }
        }

        BigDecimal height = BigDecimal.ZERO;
        String heightString = itemHeightTextField.getText();
        if(widthString.isEmpty()) {
            errorMsg.append("Height cannot be empty!\n");
        }
        else {
            try {
                height = new BigDecimal(heightString);
            }
            catch (NumberFormatException ex) {
                errorMsg.append("Height is not in valid format!\n");
            }
        }

        BigDecimal length = BigDecimal.ZERO;
        String lengthString = itemLengthTextField.getText();
        if(lengthString.isEmpty()) {
            errorMsg.append("Length cannot be empty!\n");
        }
        else {
            try {
                length = new BigDecimal(lengthString);
            }
            catch (NumberFormatException ex) {
                errorMsg.append("Length is not in valid format!\n");
            }
        }

        BigDecimal productionCost = BigDecimal.ZERO;
        String productionCostString = itemProductionCostTextField.getText();
        if(lengthString.isEmpty()) {
            errorMsg.append("Production Cost cannot be empty!\n");
        }
        else {
            try {
                productionCost = new BigDecimal(productionCostString);
            }
            catch (NumberFormatException ex) {
                errorMsg.append("Production Cost is not in valid format!\n");
            }
        }

        BigDecimal sellingPrice = BigDecimal.ZERO;
        String sellingPriceString = itemSellingPriceTextField.getText();
        if(sellingPriceString.isEmpty()) {
            errorMsg.append("Selling Price cannot be empty!\n");
        }
        else {
            try {
                sellingPrice = new BigDecimal(sellingPriceString);
            }
            catch (NumberFormatException ex) {
                errorMsg.append("Selling Price is not in valid format!\n");
            }
        }


        if(errorMsg.isEmpty()){

            Item newItem;

            newItem = Database.insertItemToDatabase(catId,name,width, height, length, productionCost, sellingPrice);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save successful!");
            alert.setHeaderText("Item data saved!");
            alert.setContentText("Item: " + name + " saved to the database!");

            alert.showAndWait();

            SearchItemController.itemList.add(newItem);

        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save failed!");
            alert.setHeaderText("Item data not saved!");
            alert.setContentText(errorMsg.toString());

            alert.showAndWait();
        }

    }

}
