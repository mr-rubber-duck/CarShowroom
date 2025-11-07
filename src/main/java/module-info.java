module org.example.carshowroom {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;

    opens org.example.carshowroom to javafx.fxml;
    exports org.example.carshowroom;
    opens org.example.carshowroom.controller to javafx.fxml;
    exports org.example.carshowroom.controller;
}