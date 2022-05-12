package com.example.inventoryapp;


import com.example.inventoryapp.model.*;
import database.Database;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class SearchStoreController {



    List<Category> catList = Database.getCategoriesFromDataBase();

    List<Store> storeList = Database.getStoreFromDataBase();

    List<Item> itemList = Database.getItemsFromDataBase(catList);

    public SearchStoreController() throws SQLException, IOException {


    }

    @FXML
    private TextField storeNameFieldText;

    @FXML
    private TextField storeItemFieldText;

    @FXML
    private TableView<Store> storeTableView;

    @FXML
    private TableColumn<Store, String> storeNameTableColumn;

    @FXML
    private TableColumn<Store, String> storeWebAddressTableColumn;

    @FXML
    private TableColumn<Store, String> storeInventoryTableColumn;

    @FXML
    private void initialize() throws SQLException, IOException {


        List<Store> storeList = Database.getStoreFromDataBase();

        storeNameTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getName());
        });

        storeWebAddressTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getWebAddress());
        });

        storeInventoryTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getSetItems().toString());
        });


        ObservableList<Store> storeObservableList = FXCollections.observableList(storeList);

        storeTableView.setItems(storeObservableList);
    }

    @FXML
    protected void onSearchButtonClick(){

        List<Store> tmpList;

        String storeName = storeNameFieldText.getText().trim();
        String storeItem = storeItemFieldText.getText().trim();
            tmpList = storeList.stream()
                    .filter(s -> s.getName().toLowerCase().contains(storeName.toLowerCase()))
                    .filter(i -> i.getSetItems().toString().toLowerCase().contains(storeItem.toLowerCase()))
                    .collect(Collectors.toList());
            storeTableView.setItems(FXCollections.observableList(tmpList));

    }

}
