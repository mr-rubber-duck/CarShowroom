module org.example.carshowroom {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens org.example.carshowroom to javafx.fxml;
    exports org.example.carshowroom;
}