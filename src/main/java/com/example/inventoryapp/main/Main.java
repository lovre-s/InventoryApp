package com.example.inventoryapp.main;

import com.example.inventoryapp.enumls.City;

import com.example.inventoryapp.exception.SameCategoryError;
import com.example.inventoryapp.exception.SameItemError;
import com.example.inventoryapp.genericsi.FoodStore;
import com.example.inventoryapp.genericsi.TechicalStore;
import com.example.inventoryapp.model.*;
import com.example.inventoryapp.sort.ProductionSorter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Used for running the application.
 */
public class Main {

    private static final int NUMBER_OF_CATEGORIES = 3;
    private static final int NUMBER_OF_ITEMS = 8;
    public static final int NUMBER_OF_STORES = 2;
    public static final int NUMBER_OF_ADDRESSES = 2;

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
      Scanner input = new Scanner(System.in);



        List<Category> catList = new ArrayList<>();

        categoryfromFile(catList);


        System.out.println("Unijeli ste sljedeće kategorije:");
        for (Category category : catList) {
            System.out.println("Ime Kategorije: " + category.getName());
            System.out.println("Opis Kategorije: " + category.getDescription());
        }

        List<Item> listaItems = new ArrayList<>();

        itemsFromFile(catList, listaItems);


        System.out.println("Unijeli ste sljedeće Item-e:");
        for (Item item : listaItems) {
            System.out.println("Ime Item-a: " + item.getName() + ".");
            System.out.println("Pripada Kategoriji: " + item.getCategory().getName() + ".");
            System.out.println("Dimenzije Proizvoda: " + item.getWidth() + "cm x "
                    + item.getHeight() + "cm x " + item.getLength() + "cm.");
            System.out.println("Trošak Proizvodnje: " + item.getProductionCost() + "kn.");
            System.out.println("Prodajna Cijena: " + item.getSellingPrice() + "kn.");

        }

        itemDiscount(input, catList, listaItems);

        List<Item> newFoodItems = foodCalCalculation(input, catList, listaItems);


        List<Factory> factoryList = new ArrayList<>();

        Set<Item> setItems= new HashSet<>();

        Address[] addresses = new Address[NUMBER_OF_ADDRESSES];

        addressFromFile(addresses);


        System.out.println("Unijeli ste sljedeće adrese:");
        for (Address address : addresses) {
            System.out.println("Naziv Ulice: " + address.getStreet() + ".");
            System.out.println("Broj: " + address.getHouseNumber() + ".");
        }



        factoriesFromFile(addresses, listaItems, factoryList);


        Integer numberOfItems = 0;

        System.out.println("Unijeli ste sljedeće tvornice:");
        for (Factory factory : factoryList) {
            System.out.println("Naziv Tvornice: " + factory.getName() + ".");
            System.out.println("Adresa Tvornice: " + factory.getAddress().getStreet() + " "
                    + factory.getAddress().getHouseNumber() + " " + factory.getAddress().getCity());

            System.out.print("Popis Item-a: ");
            for (Item item : factory.getSetItems()) {
                System.out.print(item.getName() + ", ");
            }
            System.out.println();
        }


        List<Store> storeList = new ArrayList<>();

        storesFromFile(listaItems, storeList);

        System.out.println("Unijeli ste sljedeće Trgovine:");
        for (Store store : storeList) {
            System.out.println("Naziv Trgovine: " + store.getName() + ".");
            System.out.println("Web Adresa Trgovine: " + store.getWebAddress());


            System.out.println("Popis Item-a: ");
            for (Item item : store.getSetItems()) {
                System.out.print(item.getName() + ", ");
            }
            System.out.println();
        }


        Optional<Item> itemMaxItem = Optional.empty();
        int maxVolumeFactory = 0;
        BigDecimal maxVolume = BigDecimal.valueOf(0.0);

        for (int i = 0; i < factoryList.size(); i++) {
            Item tmpItems[] = factoryList.get(i).getSetItems().toArray(new Item[0]);
            maxVolume = tmpItems[0].getHeight().multiply(tmpItems[0].getLength()
                    .multiply(tmpItems[0].getWidth()));
            for (int j = 0; j < tmpItems.length; j++) {
                BigDecimal itemVolume = tmpItems[j].getLength().multiply(tmpItems[j].getWidth());
                itemVolume = tmpItems[j].getHeight().multiply(itemVolume);
                if ((itemVolume.compareTo(maxVolume) == 1) || (itemVolume.compareTo(maxVolume) == 0)) {
                    maxVolume = itemVolume;
                    maxVolumeFactory = i;
                    itemMaxItem = Optional.ofNullable(tmpItems[j]);

                }
            }
        }



        System.out.println("Item pod nazivom " + itemMaxItem.toString() +
                " ima najveći volumen koji iznosi: " + maxVolume + " i nalazi se u tvornici: "
                + factoryList.get(maxVolumeFactory).getName() + ".");


        Store cheapestStore = storeList.get(0);

        BigDecimal itemPrice = BigDecimal.valueOf(0.0);
        BigDecimal minPrice = BigDecimal.valueOf(0.0);
        Item tmpItems1[] = storeList.get(0).getSetItems().toArray(new Item[0]);
        minPrice = tmpItems1[0].getSellingPrice();
        for (int i = 0; i < NUMBER_OF_STORES; i++) {
            Item tmpItems[] = storeList.get(i).getSetItems().toArray(new Item[i]);
            for (int j = 0; j < tmpItems.length; j++) {
                itemPrice = tmpItems[j].getSellingPrice();
                if (itemPrice.compareTo(minPrice) == -1) {
                    minPrice = itemPrice;
                    cheapestStore = storeList.get(i);
                }
            }
        }
        System.out.println("Najniža cijena: " + minPrice + "kn u " + cheapestStore.getName());

        highestCaloriesAndPrice(newFoodItems);

        longestExpiryDate(listaItems);

        Collections.sort(listaItems, new ProductionSorter());


        int choice = 0;

        System.out.println("Odaberite sortiranje: ");
        System.out.println("1. Uzlazno.");
        System.out.println("2. Silazno.");

        do{
            choice = enterNumberError(input);
            if(choice < 0 || choice > 2){
                System.out.println("Krivi unos!");
            }
        }
        while(choice < 0 || choice > 2);

        if(choice == 1){
            System.out.println("Ispis svih Item-a sortirani uzlazno:");
            System.out.println(listaItems);
            System.out.println("\n");
        }
        if(choice == 2){
            Collections.reverse(listaItems);
            System.out.println("Ispis svih Item-a sortirani silazno:");
            System.out.println(listaItems);
            System.out.println("\n");
        }


        Set<Item> setFoodItem = newFoodItems.stream()
                .filter(item -> item instanceof Apple || item instanceof Pear)
                .sorted(new ProductionSorter())
                .collect(Collectors.toSet());

        Map<Category, List<Item>> cheapestAndExpensive = new LinkedHashMap<>();
        List<Item> otherItems = new ArrayList<>();
        for(Item item : listaItems){
            if(cheapestAndExpensive.containsKey(item.getCategory())){
                List<Item>  ItemList = cheapestAndExpensive.get(item.getCategory());
                ItemList.add(item);
                cheapestAndExpensive.put(item.getCategory(), ItemList);
            }
            else{
                otherItems = new ArrayList<>();
                otherItems.add(item);
                cheapestAndExpensive.put(item.getCategory(), otherItems);
            }
        }


        System.out.println("Ispis iz Mape, najskupljeg i najjeftinijeg Item-a po Kategorijama: ");
        for (Map.Entry<Category, List<Item>> entry : cheapestAndExpensive.entrySet()) {
            System.out.println( entry.getValue().get(0));
            System.out.println( entry.getValue().get(entry.getValue().size()-1));
        }

        System.out.println("Najskuplji i najjeftiniji Item-i koji pripadaju Edible ili Technical: ");
        System.out.println("Ime najskuplje hrane: " + newFoodItems.get(0).getName() + ", cijena: "
                + newFoodItems.get(0).getSellingPrice() + "kn." );
        System.out.println("Ime najjeftinije hrane: " + newFoodItems.get(newFoodItems.size()-1).getName() +
                ", cijena: " + newFoodItems.get(newFoodItems.size()-1).getSellingPrice() + "kn.");

        cheapestExpensiveTech(listaItems);
        input.nextLine();

        System.out.println("Hrana");
        FoodStore<Edible> foodItemsStore = foodstoreFromFile(newFoodItems);

        System.out.println("Tech");
        TechicalStore<Technical> techItemsStore = techStoreFromFile(listaItems);

        List<Store> listStores =  new ArrayList<>(storeList);

        listStores.add(foodItemsStore);
        listStores.add(techItemsStore);

        SortItemsByStore(listStores);

        avgPriceSort(listaItems);

        avgItemsByStore(listStores);

        Optional <List<Item>> discountedItems = discountByItem(listaItems);


        listaItems.forEach(System.out::println);


        try(ObjectOutputStream objectWriter = new ObjectOutputStream(new FileOutputStream("dat/listaItems.ser"))) {
            objectWriter.writeObject(listaItems);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try(ObjectInputStream objectReader = new ObjectInputStream(new FileInputStream("dat/listaItems.ser"))) {
            List<Item> deserListItems = (List<Item>) objectReader.readObject();
            deserListItems.forEach(System.out::println);
        }
        catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


        List<NamedEntity> serStoreFactory = new ArrayList<>();

        for(Factory factory : factoryList){
            if(factory.getSetItems().size() >= 5){
                serStoreFactory.add(factory);
            }
        }
        for(Store store : listStores){
            if(store.getSetItems().size() >= 5){
                serStoreFactory.add(store);
            }
        }

        try(ObjectOutputStream objectWriter = new ObjectOutputStream(new FileOutputStream("dat/storeFactory.ser"))) {
            objectWriter.writeObject(serStoreFactory);
        } catch (IOException e) {
            e.printStackTrace();
        }


        try(ObjectInputStream objectReader = new ObjectInputStream(new FileInputStream("dat/storeFactory.ser"))) {
            List<NamedEntity> deserStoreFactory = (List<NamedEntity>) objectReader.readObject();
            System.out.println("Deserialized store and factory:");
            deserStoreFactory.forEach(System.out::println);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static List<Item> getListItems(List<Item> listaItems){
        return listaItems;
    }


    public static void cheapestExpensiveTech(List<Item> listaItems){
        BigDecimal min = BigDecimal.valueOf(0.0);
        BigDecimal max = BigDecimal.valueOf(0.0);
        Optional<Laptop> tmpLaptop1 = Optional.empty();
        Optional<Laptop> tmpLaptop2 = Optional.empty();


        for (int j = 0; j < NUMBER_OF_ITEMS; j++) {
            if (listaItems.get(j) instanceof Laptop laptop) {
                min = laptop.getSellingPrice();
                max = laptop.getSellingPrice();
                break;
            }
        }
        for (int i = 0; i < NUMBER_OF_ITEMS; i++) {
            if (listaItems.get(i) instanceof Laptop laptop) {
                if (laptop.getSellingPrice().compareTo(min) <= 0) {
                    min = laptop.getSellingPrice();
                    tmpLaptop1 = Optional.of(laptop);
                }
                if (laptop.getSellingPrice().compareTo(max) >= 0) {
                    max = laptop.getSellingPrice();
                    tmpLaptop2 = Optional.of(laptop);
                }

            }
        }

        if (min.compareTo(BigDecimal.valueOf(0.0)) > 0) {
            System.out.println("Najskuplji Laptop: " + tmpLaptop2.toString() +
                    ", cijena: " + max + "kn.");
        }
        else{
            System.out.println("Niste unijeli niti jedan Laptop.");
        }
        if (min.compareTo(BigDecimal.valueOf(0.0)) > 0) {
            System.out.println("Najjeftiniji Laptop: " + tmpLaptop1.toString() +
                    ", cijena: " + min + "kn.");
        }
        else {
            System.out.println("Niste unijeli niti jedan Laptop.");
        }
    }


    /**
     * Used for catching SameCategoryError exception.
     * @param input Scanner object used for inputting a String.
     * @param i whole ordinal number for all inputted categories.
     * @param categories Array of all categories.
     * @return Correctly parsed String.
     * @throws SameCategoryError
     */

    public static Optional<String> enterCategoryNameError(Scanner input, int i, Category[] categories) throws SameCategoryError{

        Optional<String> name = Optional.empty();
        Optional<String> description = Optional.empty();;
        boolean flag = false;

        do{
            flag = false;
            System.out.println((i + 1) + ". Unesite kategoriju.");
            name = Optional.ofNullable(input.nextLine());
            for (int h = 1; h < NUMBER_OF_CATEGORIES; h++) {
                if(categories[h - 1] != null){
                    if (name.equals(categories[h - 1].getName())) {
                        flag = true;
                        throw new SameCategoryError("Nazivi kategorija se moraju razlikovati!");
                    }
                }
            }
        }
        while(flag);

        return name;
    }

    /**
     * Reading input from categories.txt
     * @param catList list of categories.
     */
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
                catList.add( new Category(name.get(), description.get(), id));
            }

            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error");
            e.printStackTrace();
        }

    }

    /**
     * Used for choosing and calculating kilocalories and price per kilogram of an Item that belongs to Edible.
     * @param input Scanner object used for inputing a String.
     * @param catList Array of all categories.
     * @param listaItems List of all items.
     * @return Correctly parsed array of items that belong to Edible.
     */


    public static List<Item> foodCalCalculation(Scanner input, List<Category> catList, List<Item> listaItems) {
        int i = 0;

        System.out.println("Izracun kilokalorija: ");
        String odabir = "Food";
        int j = 0;
        for (Item item : listaItems) {
            if(odabir.equals(item.getCategory().getName())){
                j++;
            }
        }
        int numberOfItems = 0;

        List<Item> newFoodItems = new ArrayList<>();

        System.out.println("Kategorija " + odabir + ".");
        for (Item item : listaItems) {
            if (odabir.equals(item.getCategory().getName())) {
                System.out.println(("Ime Item-a: " + item.getName() + "."));
            }
            i++;
        }
        do {
            System.out.println("Unesite željeni broj Item-a: ");
            numberOfItems = enterNumberError(input);
            input.nextLine();
            if (numberOfItems < 1 || numberOfItems > j) {
                System.out.println("Krivi unos.");
            }
        }
        while (numberOfItems < 1 || numberOfItems > j);

        newFoodItems = new ArrayList<>();
        boolean flag1 = false;
        String odabirNamirnice;
        BigDecimal discountNumber;
        int odabirHrane = 0;

        do {
            System.out.println("Upisite ime željenog itema: ");
            odabirNamirnice = input.nextLine();
            for (Item item : listaItems) {
                if (odabirNamirnice.equals(item.getName()) && (odabir.equals(item.getCategory().getName()))) {
                    flag1 = true;
                    numberOfItems--;
                    do {
                        System.out.println("Odabrati Item s popisa 1 ili 2.");
                        System.out.println("1. Scomber Japonicus.");
                        System.out.println("2. Zeus Faber");
                        odabirHrane = enterNumberError(input);
                        input.nextLine();
                        if (odabirHrane < 1 || odabirHrane > j) {
                            System.out.println("Krivi unos.");
                        }
                    }
                    while (odabirHrane < 1 || odabirHrane > j);
                    if (odabirHrane == 1) {
                        System.out.println("Unesite masu Itema: ");
                        BigDecimal weight = BigDecimal.valueOf(enterNumberError(input));
                        input.nextLine();
                        Apple apple = new Apple(item.getName(),
                                item.getCategory(),
                                item.getWidth(), item.getHeight(), item.getLength(), item.getProductionCost(),
                                item.getSellingPrice(), weight, item.getDiscount(), item.getId());
                        newFoodItems.add(apple);
                        System.out.println("Odabrali ste item imena: " + odabirNamirnice);
                        System.out.println("Cijena namirnice: " + apple.calculatePrice());
                        System.out.println("Količina kilokalorija: " + apple.calculateKilocalories());
                    }
                    if (odabirHrane == 2) {
                        System.out.println("Unesite masu Itema: ");
                        BigDecimal weight = BigDecimal.valueOf(enterNumberError(input));
                        input.nextLine();
                        Pear pear = new Pear(item.getName(), item.getCategory(),
                                item.getWidth(), item.getHeight(), item.getLength(), item.getProductionCost(),
                                item.getSellingPrice(), weight, item.getDiscount(), item.getId());
                        newFoodItems.add(pear);
                        System.out.println("Odabrali ste item imena: " + odabirNamirnice);
                        System.out.println("Cijena namirnice: " + pear.calculatePrice());
                        System.out.println("Količina kilokalorija: " + pear.calculateKilocalories());
                    }
                }
            }

            if (!flag1) {
                System.out.println("Krivi unos.");
            }
        }
        while (numberOfItems != 0);


        return newFoodItems;

    }

    public static void itemDiscount(Scanner input, List<Category> catList, List<Item> listaItems){

        int i = 0;
        System.out.println("Unesene Kategorije:");
        for (Category category : catList) {
            System.out.println(" " + (i + 1) + ".");
            System.out.println("Ime Kategorije: " + category.getName() + ".");
            System.out.println("Opis Kategorije: " + category.getDescription() + ".");
            i++;
        }
        String odabir;
        int j = 0;
        boolean flag = false;
        do {
            System.out.println("Upišite ime željene kategorije: ");
            odabir = input.nextLine();
            for (i = 0; i < catList.size(); i++) {
                if (odabir.equals(catList.get(i).getName())) {
                    flag = true;
                    for (Item item : listaItems) {
                        if (odabir.equals(item.getCategory().getName())) {
                            j++;
                        }
                    }
                }
            }
            if (!flag) {
                System.out.println("Krivi Unos.");
            }
        }
        while (!flag);

        System.out.println("Kategorija " + odabir + ".");
        for (Item item : listaItems) {
            if (odabir.equals(item.getCategory().getName())) {
                System.out.println(("Ime Item-a: " + item.getName() + "."));
            }
        }
        int numberOfItems = 0;
        do {
            System.out.println("Unesite željeni broj Item-a: ");
            numberOfItems = enterNumberError(input);
            input.nextLine();
            if (numberOfItems < 1 || numberOfItems > j) {
                System.out.println("Krivi unos.");
            }
        }
        while (numberOfItems < 1 || numberOfItems > j);

        String itemChoice;
        flag = false;
        do {
            System.out.println("Upisite ime željenog itema: ");
            itemChoice = input.nextLine();
            for (Item item : listaItems) {
                if (itemChoice.equals(item.getName()) && (odabir.equals(item.getCategory().getName()))) {
                    flag = true;
                    numberOfItems--;
                    BigDecimal discountPrice;
                    BigDecimal tmpDiscount;
                    tmpDiscount = (item.getSellingPrice().multiply(item.getDiscount().discountAmount()))
                            .divide(BigDecimal.valueOf(100));
                    discountPrice = item.getSellingPrice().subtract(tmpDiscount);
                    System.out.println("Cijena Item-a s popustom: " + discountPrice + "kn.");
                }
            }
            if (!flag) {
                System.out.println("Krivi unos.");
            }

        }
        while (numberOfItems != 0);

    }


    /**
     * Reading input from items.txt.
     * @param catList list of categories.
     * @param listaItems list of all items.
     */
    public static void itemsFromFile(List<Category> catList, List<Item> listaItems){

        long id = 0;
        Optional<String> name;
        Optional<Category> category = Optional.empty();
        String tmpCat;
        BigDecimal width = BigDecimal.valueOf(0.0);
        BigDecimal height = BigDecimal.valueOf(0.0);
        BigDecimal length = BigDecimal.valueOf(0.0);
        BigDecimal productionCost = BigDecimal.valueOf(0.0);
        BigDecimal sellingPrice =  BigDecimal.valueOf(0.0);
        BigDecimal itemDiscount = BigDecimal.valueOf(0.0);
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


    /**
     * Reading input from adresses.txt
     * @param addresses array of  addresses.
     */
    public static void addressFromFile(Address[] addresses ){

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


    /**
     * Reading input from factories.txt
     * @param addresses array of adresses.
     * @param listaItems list of all items.
     * @param factoryList list of factories.
     */
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

    /**
     * Reading input from stores.txt
     * @param listaItems list of items.
     * @param storeList list of stores.
     */
    public static void storesFromFile(List<Item> listaItems, List<Store> storeList){

        Map<Long, Item> storeItems = new HashMap<>();
        for(Item item : listaItems){
            storeItems.put(item.getId(),item);
        }
        List<Item> newItems = new ArrayList<>();

        try {
            File file = new File("dat/stores.txt");
            Scanner reader = new Scanner(file);

            for(int i = 0; i < storeList.size(); i++){
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

    List<Item> newFoodItems = new ArrayList<>();
    /**
     * Used for calculating which item has highest amount of kilocalories.
     * @param newFoodItems Array of items that belong to Edible interface.
     */
    public static void highestCaloriesAndPrice(List<Item> newFoodItems) {
        int max = 0;

        Optional<Item> maxCaloriesItem = Optional.empty();

        for (Item foodItem : newFoodItems) {
            max = ((Edible) newFoodItems.get(0)).calculateKilocalories();
            if (((Edible) foodItem).calculateKilocalories() >= max) {
                max = ((Edible) foodItem).calculateKilocalories();
                maxCaloriesItem = Optional.of(foodItem);
            }

        }
        BigDecimal maxFoodPrice = BigDecimal.valueOf(0.0);
        Optional<Item> highestPriceFood = Optional.empty();
        for (Item newFoodItem : newFoodItems) {
            maxFoodPrice = ((Edible) newFoodItems.get(0)).calculatePrice();
            if ((((Edible) newFoodItem).calculatePrice().compareTo(maxFoodPrice)) == 1 ||
                    (((Edible) newFoodItem).calculatePrice().compareTo(maxFoodPrice)) == 0) {
                maxFoodPrice = ((Edible) newFoodItem).calculatePrice();
                highestPriceFood = Optional.of(newFoodItem);
            }

        }
        if ((maxFoodPrice.compareTo(BigDecimal.valueOf(0)) > 0)) {
            System.out.println("Item: " + highestPriceFood + " ima najnižu cijenu koja iznosi "
                    + maxFoodPrice + " po kilogramu.");
        }

        if (max > 0) {
            System.out.println("Item s najviše kalorija je: " + maxCaloriesItem
                    + " koja ima " + max + " kilokalorija.");
        } else {
            System.out.println("Niste unijeli Item-e pod kategorijom Hrana" +
                    " pa nema izračuna kalorija i cijene po kilogramu");
        }
    }

    /**
     * Used for calculating which laptop has the shortest expiry date.
     * @param listaItems Array of all items.
     */
    public static void longestExpiryDate(List<Item> listaItems) {
        int min = 0;
        Optional<Laptop> tmpLaptop = Optional.empty();

        for (int j = 0; j < NUMBER_OF_ITEMS; j++) {
            if (listaItems.get(j) instanceof Laptop laptop) {
                min = laptop.expiryDateAfter();
                break;
            }
        }
        for (int i = 0; i < NUMBER_OF_ITEMS; i++) {
            if (listaItems.get(i) instanceof Laptop laptop) {
                if (laptop.expiryDateAfter() <= min) {
                    min = laptop.expiryDateAfter();
                    tmpLaptop = Optional.of(laptop);
                }
            }

        }
        if (min > 0) {
            System.out.println("Laptop s najkraćom garancijom: " + tmpLaptop +
                    " koja iznosi: " + min + " mjeseci.");
        }
        else {
            System.out.println("Niste unijeli niti jedan Laptop.");
        }
    }

    /**
     * Used for catching an exception
     * @param input Scanner object used for inputing a String.
     * @return Correctly parsed whole number.
     */
    public static int enterNumberError(Scanner input) {
        Integer number = 0;
        boolean flag = false;
        do {
            flag = false;
            try {
                number = input.nextInt();
                logger.info("Unesen je broj: " + number);
            } catch (InputMismatchException ex) {
                input.nextLine();
                flag = true;
                System.out.println("Input mismatch, Input must be a number!");
                logger.error("Krivi unos! Morate unijeti broj", ex);
            }

        }
        while (flag);

        return number;
    }

    /**
     * Used for catching SameItemError exception.
     * @param input Scanner object used for inputing a String.
     * @param numberOfItems Cijeli broj kojim odredujemo koliko Item-a unosimo u Factory.
     * @param newItems List of items that are stored in a Factory.
     * @param listaItems Set of all items.
     * @return Correctly parsed String.
     * @throws SameItemError Used for catching an exception if two of the same items are inputted into a Factory.
     */
    public static Optional<String> enterItemNameError(Scanner input, int numberOfItems,
                                                      List<Item> newItems, List<Item> listaItems) throws SameItemError{

        Optional<String> itemName = Optional.empty();

        boolean flag = false;

        itemName = Optional.ofNullable(input.nextLine());

        for (Item item : listaItems) {
            if (itemName.equals(item.getName())) {
                flag = true;
                for (Item tmpitem : newItems) {
                    if(newItems.size() == 0){
                        break;
                    }
                    if(itemName.equals(tmpitem.getName())) {
                        flag = false;
                        throw new SameItemError("Nije dopusten unos istih Item-a u Factory ili Store!");
                    }
                }
                newItems.add(item);
            }
        }
        if (!flag) {
            System.out.println("Krivi unos, ponovite unos!");
            itemName = Optional.of("greska");
            return itemName;
        }
        else{
            return itemName;
        }

    }

    /**
     * Used for inputting foodStore.txt.
     * @param newFoodItems List of all Items that extend Edible.
     * @return object of FoodStore class.
     */

    public static FoodStore<Edible> foodstoreFromFile(List<Item> newFoodItems) {

        long id = 0;
        Optional<String> name = Optional.empty();
        Optional<String> webAddress = Optional.empty();
        List<Edible> foodStoreList = new ArrayList<>();

        try {
            File file = new File("dat/foodStore.txt");
            Scanner reader = new Scanner(file);

            id = reader.nextLong();
            reader.nextLine();
            name = Optional.ofNullable(reader.nextLine());
            System.out.println(name);
            webAddress = Optional.ofNullable(reader.nextLine());
            System.out.println(webAddress);


            foodStoreList = new ArrayList<>();

            for(Item item : newFoodItems){
                foodStoreList.add((Edible)item);
            }
            System.out.println(foodStoreList);

            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error");
            e.printStackTrace();
        }

        return new FoodStore<>(name.get(),webAddress.get(),foodStoreList, new HashSet<>(newFoodItems),id);
    }

    /**
     * Reading input from techStore.txt
     * @param listaItems list of items.
     * @return object of TechStore class.
     */

    public static TechicalStore<Technical> techStoreFromFile(List<Item> listaItems) {

        long id = 0;
        Optional<String> name = Optional.empty();
        Optional<String> webAddress = Optional.empty();
        List<Technical> techStoreList = new ArrayList<>();

        try {
            File file = new File("dat/techStore.txt");
            Scanner reader = new Scanner(file);

            id = reader.nextLong();
            reader.nextLine();
            name = Optional.ofNullable(reader.nextLine());
            System.out.println(name);
            webAddress = Optional.ofNullable(reader.nextLine());
            System.out.println(webAddress);

            techStoreList = new ArrayList<>();

            for(Item item : listaItems){
                if(item instanceof Technical){
                    techStoreList.add((Technical) item);
                }
            }
            System.out.println(techStoreList);


            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error");
            e.printStackTrace();
        }

        return new TechicalStore<>(name.get(),webAddress.get(),techStoreList, new HashSet<>(techStoreList.stream()
                .map(i -> (Item)i).collect(Collectors.toList())), id);

    }

    /**
     * Sorting items by Store
     * @param listaStores list of all stores.
     */
    public static void SortItemsByStore(List<Store> listaStores){

        Instant start = Instant.now();

        int i = 0;
        for(Store store : listaStores){
            List<Item> currentStore = new ArrayList<>( store.getSetItems().stream().toList());
            currentStore.sort( (Item i1,Item i2) -> {
                return i1.getHeight().multiply(i1.getLength()).multiply(i1.getWidth())
                        .compareTo(i2.getHeight().multiply(i2.getLength()).multiply(i2.getWidth()));
            });
            i++;
            System.out.println("Sortiranje Item-a po Storeovima : " + i + ".");
            System.out.println(currentStore);
        }

        Instant end = Instant.now();
        System.out.println("Trajanje sortiranja putem Lambde :" + Duration.between(start, end) + " " + end.getNano() + "nano");

        System.out.println("Sortiran listItems po volumenu: ");

    }

    /**
     * Used for calculating average price of an item that has above average volume.
     * @param listaItems List of items.
     */
    public static void avgPriceSort(List<Item> listaItems){


        BigDecimal avgVolume = listaItems.stream().map( i1 -> i1.getHeight().multiply(i1.getLength().multiply(i1.getWidth())))
                .reduce(BigDecimal.ZERO, BigDecimal::add).divide(new BigDecimal(listaItems.size())) ;


        List<BigDecimal> pricesAboveAverage = listaItems.stream()
                .filter( i1 -> i1.getHeight().multiply(i1.getLength().multiply(i1.getWidth())).compareTo(avgVolume) > 0)
                .map(i1 -> i1.getSellingPrice() ).collect(Collectors.toList());

        BigDecimal avgPrice = new BigDecimal(pricesAboveAverage.stream().mapToDouble(i1 -> i1.doubleValue()).sum());
        System.out.println("Prosjecna cijena artikala koji imaju volumen veci od prosjeka : " + avgPrice );

    }

    public static void avgItemsByStore(List<Store> listStores){



        OptionalDouble avgItemNumber = listStores.stream()
                .mapToDouble(store -> Double.valueOf(store.getSetItems().size()))
                .average();

        List<Store> storesWithAboveAverageItemCount = listStores.stream()
                .filter( store -> Double.valueOf(store.getSetItems().size()) > avgItemNumber.getAsDouble())
                .toList();
    }

    /**
     * Used for filter Items with discount amount greater than zero.
     * @param listaItems List of Items.
     */
    public static Optional <List<Item>>  discountByItem(List<Item> listaItems){

        List<Item> discountedItems  = listaItems.stream()
                .filter(item -> item.getDiscount().discountAmount().compareTo(BigDecimal.valueOf(0)) > 0)
                .collect(Collectors.toList());

        if(discountedItems.size() == 0){
            return Optional.empty();
        }
        return Optional.of(discountedItems);

    }



}