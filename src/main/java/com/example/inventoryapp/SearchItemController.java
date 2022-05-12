package com.example.inventoryapp;


import com.example.inventoryapp.model.*;
import database.Database;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import java.util.Optional;
import java.util.stream.Collectors;


public class SearchItemController{


    List<Category> catList = Database.getCategoriesFromDataBase();

    public static List<Item> itemList;

    static {
        try {
            List<Category> catList = Database.getCategoriesFromDataBase();
            itemList = Database.getItemsFromDataBase(catList);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }


    public SearchItemController() throws SQLException, IOException {
        Database.getCategoriesFromDataBase();
        Database.getItemsFromDataBase(catList);
    }



    @FXML
    private ChoiceBox<String> itemChoiceBox;

    @FXML
    private TextField itemNameFieldText;

    @FXML
    private TableColumn<Item, String> itemNameTableColumn;

    @FXML
    private TableColumn<Item, String> itemCatTableColumn;

    @FXML
    private TableColumn<Item, String> itemWidthTableColumn;

    @FXML
    private TableColumn<Item, String> itemHeightTableColumn;

    @FXML
    private TableColumn<Item, String> itemLengthTableColumn;

    @FXML
    private TableColumn<Item, String> itemProductionCostTableColumn;

    @FXML
    private TableColumn<Item, String> itemSellingPriceTableColumn;

    @FXML
    private TableView<Item> itemTableView;


    public List<String> choiceBoxItems(){
        List<String> choiceBoxItem = new ArrayList<>();
        for(Category category : catList ){
            choiceBoxItem.add(category.getName());
        }
        return choiceBoxItem;
    }


    @FXML
    private void initialize() throws SQLException, IOException {

        itemList.clear();

        itemList = Database.getItemsFromDataBase(catList);


        itemNameTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getName());
        });
        itemCatTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getCategory().getName());
        });
        itemWidthTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getWidth().toString());
        });
        itemHeightTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getHeight().toString());
        });
        itemLengthTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getLength().toString());
        });
        itemProductionCostTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getProductionCost().toString());
        });
        itemSellingPriceTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getSellingPrice().toString());
        });

        itemChoiceBox.getItems().addAll(choiceBoxItems());

        ObservableList<Item> itemObservableList = FXCollections.observableList(itemList);

        itemTableView.setItems(itemObservableList);



    }



    @FXML
    protected void onSearchButtonClick(){

        List<Item> tmpList = itemList;


        String itemName = itemNameFieldText.getText().trim();
        Optional<String> choice = Optional.ofNullable(itemChoiceBox.getValue());


        if((itemNameFieldText.getText().isEmpty() == false) && (choice.isEmpty())){
            tmpList = itemList.stream()
                    .filter(i -> i.getName().toLowerCase().contains(itemName.toLowerCase()))
                    .collect(Collectors.toList());

        }

        else if((choice.isPresent()) && (itemNameFieldText.getText().isEmpty())) {
            for (Category category : catList) {
                if (itemChoiceBox.getValue().equals(category.getName())) {
                    tmpList = itemList.stream()
                            .filter(c -> c.getCategory().getName().toLowerCase().contains(choice.get().toLowerCase()))
                            .collect(Collectors.toList());

                }

            }
        }
        else if((itemNameFieldText.getText().isEmpty() == false) && (choice.isPresent())){
            for (Category category : catList) {
                if (choice.get().equals(category.getName())) {
                    tmpList = itemList.stream()
                            .filter(c -> c.getCategory().getName().toLowerCase().contains(choice.get().toLowerCase()))
                            .filter(i -> i.getName().toLowerCase().contains(itemName.toLowerCase()))
                            .collect(Collectors.toList());

                }

            }
        }


        itemTableView.setItems(FXCollections.observableList(tmpList));

    }

}

