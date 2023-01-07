module com.example.hafta11deneme {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.hafta11deneme to javafx.fxml;
    exports com.example.hafta11deneme;
}