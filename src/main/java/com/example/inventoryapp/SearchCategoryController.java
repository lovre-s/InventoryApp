package com.example.inventoryapp;

import com.example.inventoryapp.model.Category;

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


public class SearchCategoryController {


    public static List<Category> catList;

    static {
        try {
            catList = Database.getCategoriesFromDataBase();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }



    public SearchCategoryController() {

    }

    @FXML
    private TextField catNameTextField;

    @FXML
    private TextField catDescriptionTextField;

    @FXML
    private TableColumn<Category, String> catNameTableColumn;

    @FXML
    private TableColumn<Category, String> catDescriptionTableColumn;

    @FXML
    private TableView<Category> categoryTableView;


    @FXML
    private void initialize() throws SQLException, IOException {



        catList = Database.getCategoriesFromDataBase();

        System.out.println(catList);


        catNameTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getName());
        });

        catDescriptionTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getDescription());
        });

        ObservableList<Category> categoryObservableList = FXCollections.observableList(catList);


        categoryTableView.setItems(categoryObservableList);
    }

    @FXML
    protected void onSearchButtonClick(){

        String categoryName = catNameTextField.getText().trim();

        String categoryDescription = catDescriptionTextField.getText().trim();

        List<Category> tmpList = catList.stream()
                .filter(c -> c.getName().toLowerCase().contains(categoryName.toLowerCase()))
                .filter(c -> c.getDescription().toLowerCase().contains(categoryDescription.toLowerCase()))
                .collect(Collectors.toList());
        categoryTableView.setItems(FXCollections.observableList(tmpList));
    }


}
