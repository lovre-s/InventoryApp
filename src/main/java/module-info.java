module com.example.sarlija7 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.sql;


    opens com.example.inventoryapp to javafx.fxml;
    exports com.example.inventoryapp;
}