package database;


import com.example.inventoryapp.model.*;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class Database {

    public static Connection connectToDatabase() throws SQLException, IOException {

        Properties configuration = new Properties();
        configuration.load(new FileReader("dat/database.properties"));

        String databaseURL = configuration.getProperty("databaseURL");
        String databaseUsername = configuration.getProperty("databaseUsername");
        String databasePassword = configuration.getProperty("databasePassword");

        Connection connection = DriverManager
                .getConnection(databaseURL, databaseUsername, databasePassword);

        return connection;
    }


    public static List<Category> getCategoriesFromDataBase() throws SQLException,IOException{
        Connection connection = connectToDatabase();
        List<Category> catList = new ArrayList<>();
        Statement sqlStatement = connection.createStatement();
        ResultSet categoryResultSet = sqlStatement.executeQuery( "SELECT * FROM category");

        while(categoryResultSet.next()) {
            Long categoryId = categoryResultSet.getLong("ID");
            String categoryName = categoryResultSet.getString("NAME");
            String categoryDescription = categoryResultSet.getString("DESCRIPTION");
            Category newCategory = new Category(categoryName, categoryDescription, categoryId);
            catList.add(newCategory);
        }
        connection.close();

        return catList;
    }

    public static List<Item> getItemsFromDataBase(List<Category> catList) throws SQLException,IOException{
        Connection connection = connectToDatabase();
        List<Item> itemList = new ArrayList<>();
        Statement sqlStatement = connection.createStatement();
        ResultSet itemResultSet = sqlStatement.executeQuery( "SELECT * FROM item");

        while(itemResultSet.next()) {
            Long itemId = itemResultSet.getLong("ID");
            Long itemCatId = itemResultSet.getLong("CATEGORY_ID");

            Optional<Category> itemCategory = Optional.empty();

            for(Category category : catList){
                if(itemCatId.equals(category.getId())){
                    Long catId = category.getId();
                    String name = category.getName();
                    String description = category.getDescription();
                    itemCategory = Optional.of(new Category(name, description, catId));
                }
            }
            String itemName = itemResultSet.getString("NAME");
            BigDecimal width = itemResultSet.getBigDecimal("WIDTH");
            BigDecimal height = itemResultSet.getBigDecimal("HEIGHT");
            BigDecimal length = itemResultSet.getBigDecimal("LENGTH");
            BigDecimal productionCost = itemResultSet.getBigDecimal("PRODUCTION_COST");
            BigDecimal sellingPrice = itemResultSet.getBigDecimal("SELLING_PRICE");

            Item newItem = new Item(itemName, itemCategory.get(), width, height, length, sellingPrice, productionCost, itemId);
            itemList.add(newItem);
        }
        connection.close();

        return itemList;
    }

    public static List<Address> getAddressesFromDataBase() throws SQLException,IOException{
        Connection connection = connectToDatabase();
        List<Address> addressList = new ArrayList<>();
        Statement sqlStatement = connection.createStatement();
        ResultSet addressResultSet = sqlStatement.executeQuery( "SELECT * FROM address");

        while(addressResultSet.next()) {
            Long id = addressResultSet.getLong("ID");
            String streetName = addressResultSet.getString("STREET");
            String houseNumber = addressResultSet.getString("HOUSE_NUMBER");
            String city = addressResultSet.getString("CITY");
            String postalCode = addressResultSet.getString("POSTAL_CODE");

            Address newAddress = new Address(streetName, houseNumber, city, postalCode, id);
            addressList.add(newAddress);
        }
        connection.close();

        return addressList;
    }

    public static List<Factory> getFactoryFromDataBase(List<Address> addressList) throws SQLException,IOException{
        Connection connection = connectToDatabase();
        List<Factory> factoryList = new ArrayList<>();
        Statement sqlStatement = connection.createStatement();
        ResultSet factoryResultSet = sqlStatement.executeQuery( "SELECT * FROM factory");


        while(factoryResultSet.next()) {
            Long factoryId = factoryResultSet.getLong("ID");
            String factoryName = factoryResultSet.getString("NAME");
            Long factoryAddId = factoryResultSet.getLong("ADDRESS_ID");
            Address tmpAddress = addressList.stream()
                    .filter(address -> address.getId() == factoryAddId)
                    .findFirst()
                    .get();

            Factory newFactory = new Factory(factoryName, factoryId, tmpAddress);

            getItemsToFactory(getCategoriesFromDataBase(),newFactory);

            factoryList.add(newFactory);

        }
        connection.close();

        return factoryList;
    }

    public static List<Store> getStoreFromDataBase() throws SQLException,IOException{
        Connection connection = connectToDatabase();
        List<Store> storeList = new ArrayList<>();
        Statement sqlStatement = connection.createStatement();
        ResultSet storeResultSet = sqlStatement.executeQuery( "SELECT * FROM store");

        while(storeResultSet.next()) {
            Long storeId = storeResultSet.getLong("ID");
            String storeName = storeResultSet.getString("NAME");
            String webAddress = storeResultSet.getString("WEB_ADDRESS");

            Store newStore = new Store(storeName, webAddress,storeId);

            getItemsToStore(newStore,getCategoriesFromDataBase());

            storeList.add(newStore);
        }
        connection.close();


        return storeList;
    }

    public static void getItemsToFactory(List<Category> catList,Factory newFactory) throws SQLException,IOException{
        Connection connection = connectToDatabase();
        List<Item> factoryItemList = new ArrayList<>();
        Statement sqlStatement = connection.createStatement();
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM FACTORY_ITEM FI, ITEM I WHERE FI.FACTORY_ID = ? AND FI.ITEM_ID = I.ID;");


            stmt.setLong(1,newFactory.getId());
            ResultSet factoryItemResultSet = stmt.executeQuery();
            while(factoryItemResultSet.next()) {
                Long itemId = factoryItemResultSet.getLong("ID");
                Long itemCatId = factoryItemResultSet.getLong("CATEGORY_ID");
                Optional<Category> itemCategory = Optional.empty();
                for(Category category : catList){
                    if(itemCatId.equals(category.getId())){
                        Long catId = category.getId();
                        String name = category.getName();
                        String description = category.getDescription();
                        itemCategory = Optional.of(new Category(name, description, catId));
                    }
                }
                String itemName = factoryItemResultSet.getString("NAME");
                BigDecimal width = factoryItemResultSet.getBigDecimal("WIDTH");
                BigDecimal height = factoryItemResultSet.getBigDecimal("HEIGHT");
                BigDecimal length = factoryItemResultSet.getBigDecimal("LENGTH");
                BigDecimal productionCost = factoryItemResultSet.getBigDecimal("PRODUCTION_COST");
                BigDecimal sellingPrice = factoryItemResultSet.getBigDecimal("SELLING_PRICE");

                Item newItem = new Item(itemName, itemCategory.get(), width, height, length, sellingPrice, productionCost, itemId);

                factoryItemList.add(newItem);
            }
        Set<Item> tmpItems = new HashSet<>(factoryItemList);
        newFactory.setSetItems(tmpItems);

        connection.close();

    }

    public static List<Item> getItemsToStore(Store store, List<Category> catList) throws SQLException,IOException{
        Connection connection = connectToDatabase();
        List<Item> storeItemList = new ArrayList<>();
        Statement sqlStatement = connection.createStatement();
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM STORE_ITEM SI, ITEM I WHERE SI.STORE_ID = ? AND SI.ITEM_ID = I.ID;");


            stmt.setLong(1,store.getId());
            ResultSet storeItemResultSet = stmt.executeQuery();
            while(storeItemResultSet.next()) {
                Long itemId = storeItemResultSet.getLong("ID");
                Long itemCatId = storeItemResultSet.getLong("CATEGORY_ID");
                Optional<Category> itemCategory = Optional.empty();
                for(Category category : catList){
                    if(itemCatId.equals(category.getId())){
                        Long catId = category.getId();
                        String name = category.getName();
                        String description = category.getDescription();
                        itemCategory = Optional.of(new Category(name, description, catId));
                    }
                }
                String itemName = storeItemResultSet.getString("NAME");
                BigDecimal width = storeItemResultSet.getBigDecimal("WIDTH");
                BigDecimal height = storeItemResultSet.getBigDecimal("HEIGHT");
                BigDecimal length = storeItemResultSet.getBigDecimal("LENGTH");
                BigDecimal productionCost = storeItemResultSet.getBigDecimal("PRODUCTION_COST");
                BigDecimal sellingPrice = storeItemResultSet.getBigDecimal("SELLING_PRICE");

                Item newItem = new Item(itemName, itemCategory.get(), width, height, length, sellingPrice, productionCost, itemId);
                storeItemList.add(newItem);
            }
        Set<Item> tmpItems = new HashSet<>(storeItemList);
        store.setSetItems(tmpItems);

        connection.close();

        return storeItemList;
    }

    public static Optional<Category> getCategoryById(List<Category> catList) throws SQLException,IOException{
        Connection connection = connectToDatabase();
        Statement sqlStatement = connection.createStatement();
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM CATEGORY WHERE ID = ?;");
        Optional<Category> newCategory = Optional.empty();
        for(Category category : catList){
            stmt.setLong(1,category.getId());
            ResultSet categoryByIdResultSet = stmt.executeQuery();
            while(categoryByIdResultSet.next()) {
                Long categoryId = categoryByIdResultSet.getLong("ID");
                String categoryName = categoryByIdResultSet.getString("NAME");
                String categoryDescription = categoryByIdResultSet.getString("DESCRIPTION");
                newCategory = Optional.of(new Category(categoryName, categoryDescription, categoryId));
            }
        }
        connection.close();

        return newCategory;
    }


    public static Optional<Item> getItemById(List<Item> itemList, List<Category> catList) throws SQLException,IOException{
        Connection connection = connectToDatabase();
        Statement sqlStatement = connection.createStatement();
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM ITEM WHERE ID = ?;");
        Optional<Item> newItem = Optional.empty();
        for(Item item : itemList){
            stmt.setLong(1,item.getId());
            ResultSet itembyIdResultSet = stmt.executeQuery();
            while(itembyIdResultSet.next()) {
                Long itemId = itembyIdResultSet.getLong("ID");
                Long itemCatId = itembyIdResultSet.getLong("CATEGORY_ID");
                Optional<Category> itemCategory = Optional.empty();
                for(Category category : catList){
                    if(itemCatId.equals(category.getId())){
                        Long catId = category.getId();
                        String name = category.getName();
                        String description = category.getDescription();
                        itemCategory = Optional.of(new Category(name, description, catId));
                    }
                }
                String itemName = itembyIdResultSet.getString("NAME");
                BigDecimal width = itembyIdResultSet.getBigDecimal("WIDTH");
                BigDecimal height = itembyIdResultSet.getBigDecimal("HEIGHT");
                BigDecimal length = itembyIdResultSet.getBigDecimal("LENGTH");
                BigDecimal productionCost = itembyIdResultSet.getBigDecimal("PRODUCTION_COST");
                BigDecimal sellingPrice = itembyIdResultSet.getBigDecimal("SELLING_PRICE");
                newItem = Optional.of(new Item(itemName, itemCategory.get(), width, height, length, sellingPrice, productionCost, itemId));
            }
        }
        connection.close();

        return newItem;
    }

    public static Optional<Factory> getFactoryById(List<Factory> factoryList, List<Address> addressList) throws SQLException,IOException{
        Connection connection = connectToDatabase();
        Statement sqlStatement = connection.createStatement();
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM FACTORY WHERE ID = ?;");
        Optional<Factory> newFactory = Optional.empty();
        for(Factory factory : factoryList){
            stmt.setLong(1,factory.getId());
            ResultSet factoryByIdResultSet = stmt.executeQuery();
            while(factoryByIdResultSet.next()) {

                Long factoryId = factoryByIdResultSet.getLong("ID");
                String factoryName = factoryByIdResultSet.getString("NAME");
                Long factoryAddId = factoryByIdResultSet.getLong("ADDRESS_ID");
                Address tmpAddress = addressList.stream()
                        .filter(address -> address.getId() == factoryAddId)
                        .findFirst()
                        .get();
                newFactory = Optional.of(new Factory(factoryName, factoryId, tmpAddress));
            }
        }
        connection.close();

        return newFactory;
    }

    public static Optional<Store> getStoreById(List<Store> storeList) throws SQLException,IOException{
        Connection connection = connectToDatabase();
        Statement sqlStatement = connection.createStatement();
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM STORE WHERE ID = ?;");
        Optional<Store> newStore = Optional.empty();
        for(Store store : storeList){
            stmt.setLong(1,store.getId());
            ResultSet storeByIdResultSet = stmt.executeQuery();
            while(storeByIdResultSet.next()) {
                Long storeId = storeByIdResultSet.getLong("ID");
                String storeName = storeByIdResultSet.getString("NAME");
                String storeWebAddress = storeByIdResultSet.getString("WEB_ADDRESS");

                newStore = Optional.of(new Store(storeName,storeWebAddress,storeId));
            }
        }
        connection.close();

        return newStore;
    }

    public static Category insertCategoryToDatabase(String name, String description) throws SQLException, IOException {
        Connection connection = connectToDatabase();
        PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO CATEGORY(NAME, DESCRIPTION) VALUES(?, ?)");



        stmt.setString(1, name);
        stmt.setString(2, description);


        Category newCategory = new Category(name,description);

        stmt.executeUpdate();
        connection.close();

        return newCategory;

    }

    public static Item insertItemToDatabase(Long catId, String name,BigDecimal width, BigDecimal height, BigDecimal length,
                                            BigDecimal productionCost, BigDecimal sellingPrice) throws SQLException, IOException {
        Connection connection = connectToDatabase();
        PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO ITEM(CATEGORY_ID, NAME, WIDTH, HEIGHT, LENGTH, PRODUCTION_COST, SELLING_PRICE)" +
                        " VALUES(?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);


        stmt.setLong(1,catId);
        stmt.setString(2,name);
        stmt.setBigDecimal(3, width);
        stmt.setBigDecimal(4, height);
        stmt.setBigDecimal(5, length);
        stmt.setBigDecimal(6, productionCost);
        stmt.setBigDecimal(7, sellingPrice);

        Item newItem = new Item(catId,name,width,height,length, productionCost, sellingPrice);

        stmt.executeUpdate();

        ResultSet keys = stmt.getGeneratedKeys();
        if(keys.next()){
            Long id = keys.getLong(1);
            newItem.setId(id);
        }


        connection.close();

        return newItem;

    }

    public static void insertAddressToDatabase(Address address) throws SQLException, IOException {
        Connection connection = connectToDatabase();
        PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO ADDRESS(STREET, HOUSE_NUMBER, CITY, POSTAL_CODE) VALUES(?, ?, ?, ?)");

        stmt.setString(1,address.getStreet());
        stmt.setString(2,address.getHouseNumber());
        stmt.setString(3, address.getGrad());
        stmt.setString(4, address.getPostalCode());

        stmt.executeUpdate();
        connection.close();
    }

    public static void insertFactoryToDatabase(Factory factory) throws SQLException, IOException {
        Connection connection = connectToDatabase();
        PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO FACTORY(NAME, ADDRESS_ID) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);

        stmt.setString(1,factory.getName());
        stmt.setLong(2,factory.getAddress().getId());

        stmt.executeUpdate();

        ResultSet keys = stmt.getGeneratedKeys();
        if(keys.next()){
            Long id = keys.getLong(1);
            factory.setId(id);
        }

        connection.close();

        insertNewFactoryItems(factory);
    }
    public static void insertNewFactoryItems(Factory factory) throws SQLException, IOException {

        Connection connection = connectToDatabase();
        PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO FACTORY_ITEM(FACTORY_ID, ITEM_ID) VALUES(?, ?)");

        for(Item item : factory.getSetItems()){
            stmt.setLong(1,factory.getId());
            stmt.setLong(2,item.getId());
            stmt.executeUpdate();
        }
        connection.close();
    }

    public static void insertNewStoreItems(Store store) throws SQLException, IOException {

        Connection connection = connectToDatabase();
        PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO STORE_ITEM(STORE_ID, ITEM_ID) VALUES(?, ?)");

        for(Item item : store.getSetItems()){
            stmt.setLong(1,store.getId());
            stmt.setLong(2,item.getId());
            stmt.executeUpdate();
        }
        connection.close();
    }

    public static void insertStoreToDatabase(Store store) throws SQLException, IOException {
        Connection connection = connectToDatabase();
        PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO STORE(NAME, WEB_ADDRESS) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);

        stmt.setString(1,store.getName());
        stmt.setString(2,store.getWebAddress());

        stmt.executeUpdate();

        ResultSet keys = stmt.getGeneratedKeys();
        if(keys.next()){
            Long id = keys.getLong(1);
            store.setId(id);
        }

        connection.close();

        insertNewStoreItems(store);
    }

    public static void insertItemToFactoryDatabase(Factory factory, Item item) throws SQLException, IOException {
        Connection connection = connectToDatabase();
        PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO FACTORY_ITEM(FACTORY_ID, ITEM_ID) VALUES(?, ?)");

        stmt.setLong(1,factory.getId());
        stmt.setLong(2, item.getId());

        stmt.executeUpdate();
        connection.close();
    }

    public static void insertItemToStoreDatabase(Store store, Item item) throws SQLException, IOException {
        Connection connection = connectToDatabase();
        PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO STORE_ITEM(STORE_ID, ITEM_ID) VALUES(?, ?)");

        stmt.setLong(1,store.getId());
        stmt.setLong(2, item.getId());

        stmt.executeUpdate();
        connection.close();


    }



}
