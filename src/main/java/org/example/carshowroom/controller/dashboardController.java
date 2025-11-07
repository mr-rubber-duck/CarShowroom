package org.example.carshowroom.controller;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class dashboardController {

    @FXML
    private StackPane contentArea;

    @FXML
    private Label dashboardLabel;

    @FXML
    public void initialize() {
        dashboardLabel.setText("Welcome to your dashboard!");
    }

    @FXML
    private void showDashboard(ActionEvent event) {
        dashboardLabel.setText("🏠 Dashboard Overview");
    }

    @FXML
    private void showCars(ActionEvent event) {
        dashboardLabel.setText("🚘 Manage Cars");
    }

    @FXML
    private void showUsers(ActionEvent event) {
        dashboardLabel.setText("👥 Manage Users");
    }

    @FXML
    private void showSales(ActionEvent event) {
        dashboardLabel.setText("💰 View Sales Reports");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Confirm Logout");
        alert.setContentText("Are you sure you want to log out?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Stage stage = (Stage) contentArea.getScene().getWindow();
                stage.close();
            }
        });
    }
}
