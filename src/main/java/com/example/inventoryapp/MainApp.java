package com.example.inventoryapp;

import com.example.inventoryapp.enumls.City;
import com.example.inventoryapp.model.*;
import database.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;


public class MainApp extends Application {

    public static Stage mainStage;

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void setMainStage(Stage mainStage) {
        MainApp.mainStage = mainStage;
    }

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("firstScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Inventory managment application");
        stage.setScene(scene);
        stage.show();
    }


    private static final int NUMBER_OF_ITEMS = 5;
    private static final int NUMBER_OF_ADDRESSES = 2;


    public static void main(String[] args) {

        try {
            System.out.println("Spojen na bazu!");



            List<Category> categoryList = Database.getCategoriesFromDataBase();
            List<Item> itemList = Database.getItemsFromDataBase(categoryList);
            List<Address> addressList = Database.getAddressesFromDataBase();
            List<Factory> factoryList = Database.getFactoryFromDataBase(addressList);
            List<Store> storeList = Database.getStoreFromDataBase();


            Optional<Item> itemById = Database.getItemById(itemList,categoryList);
            Optional<Category> categoryById = Database.getCategoryById(categoryList);
            Optional<Factory> factoryById = Database.getFactoryById(factoryList,addressList);
            Optional<Store> storeById = Database.getStoreById(storeList);



            for(Category category : categoryList){
                System.out.println(category);
            }

            for(Item item : itemList){
                System.out.println(item);
            }

            for(Address address : addressList){
                System.out.println(address);
            }

            for(Factory factory : factoryList){
                System.out.println(factory);
            }

            for(Store store : storeList){
                System.out.println(store);
            }

            System.out.println("\n Ispis Kategorije po ID-u: " + categoryById);
            System.out.println("\n Ispis item-a po ID-u: " + itemById);
            System.out.println("\n Ispis Factory-a po ID-u: " + factoryById);
            System.out.println("\n Ispis Store-a po ID-u: " + storeById);

        } catch (SQLException  | IOException e) {
            System.out.println("Pogreska kod spajanja!");
            e.printStackTrace();
        }

        launch();
    }

    public static void categoryfromFile(List<Category> catList){

        Optional<String> name;
        Optional<String> description;
        long id = 0;


        try {
            File file = new File("dat/categories.txt");
            Scanner reader = new Scanner(file);


            while(reader.hasNextLine()){
                id = reader.nextLong();
                reader.nextLine();
                System.out.println(id);
                name = Optional.ofNullable(reader.nextLine());
                System.out.println(name);
                description = Optional.ofNullable(reader.nextLine());
                System.out.println(description);
                catList.add(new Category(name.get(), description.get(), id));
            }

            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error");
            e.printStackTrace();
        }

    }

    public static void itemsFromFile(List<Category> catList, List<Item> listaItems){

        long id = 0;
        Optional<String> name;
        Optional<Category> category = Optional.empty();
        String tmpCat;
        BigDecimal width = BigDecimal.valueOf(0);
        BigDecimal height = BigDecimal.valueOf(0);
        BigDecimal length = BigDecimal.valueOf(0);
        BigDecimal productionCost = BigDecimal.valueOf(0);
        BigDecimal sellingPrice =  BigDecimal.valueOf(0);
        BigDecimal itemDiscount = BigDecimal.valueOf(0);
        int warranty = 0;

        try {
            File file = new File("dat/items.txt");
            Scanner reader = new Scanner(file).useLocale(Locale.ROOT);

            while(reader.hasNextLine()){
                id = reader.nextLong();
                reader.nextLine();
                name = Optional.ofNullable(reader.nextLine());
                tmpCat = reader.nextLine();
                width = reader.nextBigDecimal();
                reader.nextLine();
                height = reader.nextBigDecimal();
                reader.nextLine();
                length = reader.nextBigDecimal();
                reader.nextLine();
                productionCost = reader.nextBigDecimal();
                reader.nextLine();
                sellingPrice = reader.nextBigDecimal();
                reader.nextLine();
                itemDiscount = reader.nextBigDecimal();
                Discount discount = new Discount(itemDiscount);

                for(int j = 0; j < catList.size(); j++){
                    if(tmpCat.equals(catList.get(j).getName())){
                        category = Optional.ofNullable(catList.get(j));
                    }
                }
                if(category.get().getName().equals("Technical")){
                    reader.nextLine();
                    warranty = reader.nextInt();
                    listaItems.add( new Laptop(name.get(), category.get(), width, height, length, productionCost,
                            sellingPrice, discount,
                            warranty, id));
                }
                else{
                    listaItems.add( new Item(name.get(), category.get(), width, height, length, productionCost,
                            sellingPrice, discount, id));
                }

            }

            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error");
            e.printStackTrace();
        }

    }



    public static void addressFromFile(Address[] addresses){

        Optional<City> city = Optional.empty();

        try {
            File file = new File("dat/addresses.txt");
            Scanner reader = new Scanner(file);

            for(int i = 0; i < NUMBER_OF_ADDRESSES; i++){
                String street = reader.nextLine();
                System.out.println(street);
                String houseNumber  = reader.nextLine();
                System.out.println(houseNumber);
                int choice = reader.nextInt();
                reader.nextLine();
                if(choice == 1){
                    city = Optional.of(City.ZADAR);
                }
                if(choice == 2){
                    city = Optional.of(City.ZAGREB);
                }
                if(choice == 3){
                    city = Optional.of(City.MILANO);
                }

                addresses[i] = new Address.Builder(street)
                        .atHouseNumber(houseNumber)
                        .inCity(city.get())
                        .build();
            }

            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error");
            e.printStackTrace();
        }

    }

    public static void factoriesFromFile(Address[] addresses, List<Item> listaItems,  List<Factory> factoryList){

        Map<Long, Item> factoryItems = new HashMap<>();
        for(Item item : listaItems){
            factoryItems.put(item.getId(),item);
        }

        try {
            File file = new File("dat/factories.txt");
            Scanner reader = new Scanner(file);

            while(reader.hasNextLine()){
                List<Item> newItems = new ArrayList<>();
                long id = reader.nextLong();
                reader.nextLine();
                String name = reader.nextLine();
                System.out.println(name);
                int choice  = reader.nextInt();
                reader.nextLine();
                Address address = addresses[choice - 1];
                reader.nextLine();
                List<Long> artikli = List.of(reader.nextLine().split(",")).stream()
                        .map(s -> Long.parseLong(s)).collect(Collectors.toList());
                for(Long tmp : artikli){
                    newItems.add(factoryItems.get(tmp));
                }
                System.out.println(newItems);

                Set<Item> tmpItems = new HashSet<>(newItems);
                factoryList.add(new Factory(name, address, tmpItems, id));
            }

            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error");
            e.printStackTrace();
        }

    }

    public static void storesFromFile(List<Item> listaItems, List<Store> storeList){

        Map<Long, Item> storeItems = new HashMap<>();
        for(Item item : listaItems){
            storeItems.put(item.getId(),item);
        }
        List<Item> newItems = new ArrayList<>();

        try {
            File file = new File("dat/stores.txt");
            Scanner reader = new Scanner(file);

            while(reader.hasNextLine()){
                newItems = new ArrayList<>();
                long id = reader.nextLong();
                reader.nextLine();
                String name = reader.nextLine();
                System.out.println(name);
                String webAddress = reader.nextLine();
                System.out.println(webAddress);
                reader.nextLine();
                List<Long> storeArticle = List.of(reader.nextLine().split(",")).stream()
                        .map(s -> Long.parseLong(s)).collect(Collectors.toList());
                for(Long tmp : storeArticle){
                    newItems.add(storeItems.get(tmp));
                }
                System.out.println(newItems);

                Set<Item> tmpItems = new HashSet<>(newItems);
                storeList.add(new Store(name, webAddress, tmpItems, id));
            }

            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error");
            e.printStackTrace();
        }

        List<Item> StoreItems = newItems.stream()
                .filter(item -> item instanceof Technical)
                .sorted((i1, i2) -> {
                    BigDecimal volume1 = i1.getHeight().multiply(i1.getLength().multiply(i1.getWidth()));
                    BigDecimal volume2 = i2.getHeight().multiply(i2.getLength().multiply(i2.getWidth()));
                    if(volume1.compareTo(volume2) > 0){
                        return -1;
                    }
                    else if(volume1.compareTo(volume2) < 0){
                        return 1;
                    }
                    else{
                        return 0;
                    }
                }).collect(Collectors.toList());

        System.out.println("Sortirani Item-i po volumenu u Store-u: ");
        System.out.println(newItems);

        BigDecimal avg = BigDecimal.valueOf(NUMBER_OF_ITEMS);
        avg = (avg.divide(BigDecimal.valueOf(2))).subtract(BigDecimal.valueOf(0.5));

        int size = newItems.size();

        if(avg.compareTo(BigDecimal.valueOf(size)) < 0){
            System.out.println("Store ima iznadprosjecnu kolicinu Item-a.");
        }
        else{
            System.out.println("Store ima ispodprosjecnu kolicinu Item-a.");
        }



    }




}