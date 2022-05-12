package com.example.inventoryapp;

import com.example.inventoryapp.model.Category;
import database.Database;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;



public class AddNewCategoryController {


    List<Category> catList = Database.getCategoriesFromDataBase();


    public AddNewCategoryController() throws SQLException, IOException {


    }


    @FXML
    private TextField catNameTextField;

    @FXML
    private TextField catDescTextField;

    @FXML
    private void initialize() throws SQLException, IOException {

        Database.getCategoriesFromDataBase();

    }

    @FXML
    protected void onAddCatButtonClick() throws SQLException, IOException {

        StringBuilder errorMsg = new StringBuilder();

        String name = catNameTextField.getText();
        if(name.isEmpty()){
            errorMsg.append("Name cannot be empty!\n");
        }

        String description = catDescTextField.getText();
        if(description.isEmpty()){
            errorMsg.append("Description cannot be empty!\n");
        }


        if(errorMsg.isEmpty()){

            Category newCategory = new Category(name, description);

          newCategory = Database.insertCategoryToDatabase(name,description);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save successful!");
            alert.setHeaderText("Category data saved!");
            alert.setContentText("Category: " + name + " " + description + " saved to the database!");

            alert.showAndWait();

            SearchCategoryController.catList.add(newCategory);


        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save failed!");
            alert.setHeaderText("Category data not saved!");
            alert.setContentText(errorMsg.toString());

            alert.showAndWait();

        }

    }




}
