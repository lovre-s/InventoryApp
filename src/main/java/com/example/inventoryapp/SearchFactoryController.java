package com.example.inventoryapp;

import com.example.inventoryapp.model.*;
import database.Database;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SearchFactoryController {


    List<Category> catList = Database.getCategoriesFromDataBase();
    List<Address> addressList = Database.getAddressesFromDataBase();
    List<Factory> factoryList = Database.getFactoryFromDataBase(addressList);;


    List<Item> itemList = Database.getItemsFromDataBase(catList);

    public SearchFactoryController() throws SQLException, IOException {
        Database.getCategoriesFromDataBase();
        Database.getItemsFromDataBase(catList);
        Database.getAddressesFromDataBase();
        Database.getFactoryFromDataBase(addressList);


    }

    @FXML
    private ChoiceBox<String> factoryChoiceBox;

    @FXML
    private TextField factoryNameFieldText;

    @FXML
    private TableView<Factory> factoryTableView;

    @FXML
    private TableColumn<Factory, String> factoryNameTableColumn;

    @FXML
    private TableColumn<Factory, String> factoryAddressTableColumn;

    @FXML
    private TableColumn<Factory, String> factoryInventoryTableColumn;

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


        List<Factory> factoryList = Database.getFactoryFromDataBase(addressList);

        factoryNameTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getName());
        });


        factoryAddressTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getAddress().toString());
        });

        factoryInventoryTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getSetItems().toString());
        });


        factoryChoiceBox.getItems().addAll(choiceBoxItems());

        ObservableList<Factory> factoryObservableList = FXCollections.observableList(factoryList);
        factoryTableView.setItems(factoryObservableList);



    }

    @FXML
    protected void onSearchButtonClick() throws SQLException, IOException {


        List<Factory> tmpList = factoryList;

        String factoryName = factoryNameFieldText.getText().trim();
        Optional<String> choice = Optional.ofNullable(factoryChoiceBox.getValue());


        List<Address> addressList = Database.getAddressesFromDataBase();


        if((factoryNameFieldText.getText().isEmpty() == false) && (choice.isEmpty())) {
            tmpList = factoryList.stream()
                    .filter(f -> f.getName().toLowerCase().contains(factoryName.toLowerCase()))
                    .collect(Collectors.toList());

        }


        else if((choice.isPresent()) && (factoryNameFieldText.getText().isEmpty())) {
            for (Address address : addressList) {
                if (factoryChoiceBox.getValue().equals(address.getGrad().toString())) {
                    tmpList = factoryList.stream()
                            .filter(c -> c.getAddress().getGrad().toString().toLowerCase().contains(factoryChoiceBox.getValue().toLowerCase()))
                            .collect(Collectors.toList());

                }

            }
        }

        else if((factoryNameFieldText.getText().isEmpty() == false) && (choice.isPresent())){
            for (Address address : addressList) {
                if (factoryChoiceBox.getValue().equals(address.getGrad())) {
                    tmpList = factoryList.stream()
                            .filter(c -> c.getAddress().getGrad().toLowerCase().contains(factoryChoiceBox.getValue().toLowerCase()))
                            .filter(f -> f.getName().toLowerCase().contains(factoryName.toLowerCase()))
                            .collect(Collectors.toList());

                }

            }
        }

        factoryTableView.setItems(FXCollections.observableList(tmpList));

    }

}
