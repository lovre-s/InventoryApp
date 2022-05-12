package com.example.inventoryapp;



import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;


import java.io.IOException;

public class FirstScreenController {

    public void showItemSearchScreen(){
        FXMLLoader fxmlLoader =
                new FXMLLoader(MainApp.class.getResource(
                        "itemSearch.fxml"
                ));
        Scene scene = null;
        try{
            scene = new Scene(fxmlLoader.load(), 620,400);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        MainApp.getMainStage().setScene(scene);
        MainApp.getMainStage().show();
    }

    public void showCategorySearchScreen(){
        FXMLLoader fxmlLoader =
                new FXMLLoader(MainApp.class.getResource(
                        "categorySearch.fxml"
                ));
        Scene scene = null;
        try{
            scene = new Scene(fxmlLoader.load(), 610,400);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        MainApp.getMainStage().setScene(scene);
        MainApp.getMainStage().show();
    }

    public void showFactorySearchScreen(){
        FXMLLoader fxmlLoader =
                new FXMLLoader(MainApp.class.getResource(
                        "factorySearch.fxml"
                ));
        Scene scene = null;
        try{
            scene = new Scene(fxmlLoader.load(), 620,450);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        MainApp.getMainStage().setScene(scene);
        MainApp.getMainStage().show();
    }

    public void showStoreSearchScreen(){
        FXMLLoader fxmlLoader =
                new FXMLLoader(MainApp.class.getResource(
                        "storeSearch.fxml"
                ));
        Scene scene = null;
        try{
            scene = new Scene(fxmlLoader.load(), 620,450);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        MainApp.getMainStage().setScene(scene);
        MainApp.getMainStage().show();
    }

    public void showAddNewItemScreen(){
        FXMLLoader fxmlLoader =
                new FXMLLoader(MainApp.class.getResource(
                        "addNewItemScreen.fxml"
                ));
        Scene scene = null;
        try{
            scene = new Scene(fxmlLoader.load(), 620,650);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        MainApp.getMainStage().setScene(scene);
        MainApp.getMainStage().show();
    }

    public void showAddNewCategoryScreen(){
        FXMLLoader fxmlLoader =
                new FXMLLoader(MainApp.class.getResource(
                        "AddNewCategoryScreen.fxml"
                ));
        Scene scene = null;
        try{
            scene = new Scene(fxmlLoader.load(), 620,650);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        MainApp.getMainStage().setScene(scene);
        MainApp.getMainStage().show();
    }

    public void showAddNewFactoryScreen(){
        FXMLLoader fxmlLoader =
                new FXMLLoader(MainApp.class.getResource(
                        "AddNewFactoryScreen.fxml"
                ));
        Scene scene = null;
        try{
            scene = new Scene(fxmlLoader.load(), 620,650);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        MainApp.getMainStage().setScene(scene);
        MainApp.getMainStage().show();
    }

    public void showAddNewStoreScreen(){
        FXMLLoader fxmlLoader =
                new FXMLLoader(MainApp.class.getResource(
                        "AddNewStoreScreen.fxml"
                ));
        Scene scene = null;
        try{
            scene = new Scene(fxmlLoader.load(), 620,650);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        MainApp.getMainStage().setScene(scene);
        MainApp.getMainStage().show();
    }


}