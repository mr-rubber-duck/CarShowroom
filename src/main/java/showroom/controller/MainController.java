package showroom.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import showroom.dao.VehicleDAO;
import showroom.model.Vehicle;
import showroom.model.Car;
import showroom.model.Motorcycle;
import showroom.model.Truck;

import java.io.File;
import java.util.List;

public class MainController {

        @FXML
        private TableView<Vehicle> vehicleTable;
        @FXML
        private TableColumn<Vehicle, Long> colId;
        @FXML
        private TableColumn<Vehicle, String> colBrand;
        @FXML
        private TableColumn<Vehicle, String> colModel;
        @FXML
        private TableColumn<Vehicle, Integer> colYear;
        @FXML
        private TableColumn<Vehicle, Double> colPrice;
        @FXML
        private TableColumn<Vehicle, String> colStatus;
        @FXML
        private TableColumn<Vehicle, String> colImage;

        @FXML
        private VBox listViewContent;
        @FXML
        private VBox detailsViewContent;
        @FXML
        private ImageView logoImageView;

        // Detail View Fields
        @FXML
        private Label detTitle, detYear, detPrice, detColor, detId, detStatus, detType, detNoImage;
        @FXML
        private ImageView detMainImage;
        @FXML
        private HBox detThumbnails;
        @FXML
        private VBox detSpecsContainer;

        private final VehicleDAO vehicleDAO = new VehicleDAO();
        private final ObservableList<Vehicle> vehicleList = FXCollections.observableArrayList();

        @FXML
        public void initialize() {
                colId.setCellValueFactory(new PropertyValueFactory<>("id"));
                colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
                colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
                colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
                colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
                colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

                // Image Column
                colImage.setCellValueFactory(cellData -> {
                        List<String> images = cellData.getValue().getImages();
                        if (images != null && !images.isEmpty()) {
                                return new SimpleStringProperty(images.get(0));
                        }
                        return new SimpleStringProperty(null);
                });

                colImage.setCellFactory(param -> new TableCell<>() {
                        private final ImageView imageView = new ImageView();

                        @Override
                        protected void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty || item == null) {
                                        setGraphic(null);
                                        setText(null);
                                } else {
                                        try {
                                                File file = new File(item);
                                                if (file.exists()) {
                                                        Image img = new Image(file.toURI().toString(), 80, 60, true,
                                                                        true);
                                                        imageView.setImage(img);
                                                        imageView.setFitWidth(80);
                                                        imageView.setFitHeight(60);
                                                        imageView.setPreserveRatio(true);
                                                        setGraphic(imageView);
                                                } else {
                                                        setGraphic(null);
                                                }
                                        } catch (Exception e) {
                                                setGraphic(null);
                                        }
                                        setText(null);
                                }
                        }
                });

                vehicleTable.setRowFactory(tv -> {
                        TableRow<Vehicle> row = new TableRow<>();
                        row.setOnMouseClicked(event -> {
                                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                                        handleViewDetails();
                                }
                        });
                        return row;
                });

                // Load Logo
                try {
                        logoImageView.setImage(new Image(getClass().getResourceAsStream("/icon/logo.png")));
                } catch (Exception e) {
                        System.err.println("Could not load logo: " + e.getMessage());
                }

                refreshTable();
        }

        private void refreshTable() {
                vehicleList.clear();
                vehicleList.addAll(vehicleDAO.getAllVehicles());
                vehicleTable.setItems(vehicleList);
        }

        @FXML
        private void handleViewDetails() {
                Vehicle selected = vehicleTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                        populateDetails(selected);
                        listViewContent.setVisible(false);
                        detailsViewContent.setVisible(true);
                } else {
                        showAlert("No Selection", "Please select a vehicle to view details.");
                }
        }

        @FXML
        private void handleBackToList() {
                listViewContent.setVisible(true);
                detailsViewContent.setVisible(false);
                refreshTable();
        }

        private void populateDetails(Vehicle v) {
                detTitle.setText(v.getBrand() + " " + v.getModel());
                detYear.setText(String.valueOf(v.getYear()));
                detPrice.setText(String.format("$%,.2f", v.getPrice()));
                detColor.setText(v.getColor());
                detId.setText("#" + v.getId());
                detStatus.setText(v.getStatus());
                detType.setText(v.getClass().getSimpleName().toUpperCase());

                detStatus.getStyleClass().removeAll("status-badge-available", "status-badge-sold");
                if ("AVAILABLE".equals(v.getStatus())) {
                        detStatus.getStyleClass().add("status-badge-available");
                } else {
                        detStatus.getStyleClass().add("status-badge-sold");
                }

                detSpecsContainer.getChildren().clear();
                if (v instanceof Car c) {
                        addSpec("Condition", c.getCondition() != null ? c.getCondition() : "N/A");
                        if ("Used".equals(c.getCondition()) && c.getMileage() != null) {
                                addSpec("Mileage", String.format("%,d miles", c.getMileage()));
                        }
                        addSpec("Doors", String.valueOf(c.getNumberOfDoors()));
                        addSpec("Fuel", c.getFuelType() != null ? c.getFuelType() : "N/A");
                        addSpec("Transmission", c.getTransmission() != null ? c.getTransmission() : "N/A");
                        addSpec("Engine", c.getEngine() != null ? c.getEngine() : "N/A");
                        addSpec("Drivetrain", c.getDrivetrainType() != null ? c.getDrivetrainType() : "N/A");
                } else if (v instanceof Motorcycle m) {
                        addSpec("Engine", m.getEngineCapacity() + " cc");
                        addSpec("Sidecar", m.isHasSidecar() ? "Yes" : "No");
                } else if (v instanceof Truck t) {
                        addSpec("Load", t.getLoadCapacity() + " kg");
                        addSpec("Axles", String.valueOf(t.getNumberOfAxles()));
                }

                detThumbnails.getChildren().clear();
                List<String> images = v.getImages();
                if (images != null && !images.isEmpty()) {
                        detNoImage.setVisible(false);
                        detMainImage.setVisible(true);
                        loadMainImage(images.get(0));
                        for (String path : images)
                                addThumbnail(path);
                } else {
                        detNoImage.setVisible(true);
                        detMainImage.setVisible(false);
                }
        }

        private void addSpec(String k, String v) {
                HBox hb = new HBox(10);
                Label kl = new Label(k + ":");
                kl.setStyle("-fx-text-fill: gray;");
                Label vl = new Label(v);
                vl.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
                hb.getChildren().addAll(kl, vl);
                detSpecsContainer.getChildren().add(hb);
        }

        private void addThumbnail(String p) {
                try {
                        File f = new File(p);
                        if (f.exists()) {
                                ImageView iv = new ImageView(new Image(f.toURI().toString(), 60, 60, true, true));
                                iv.setStyle("-fx-cursor: hand; -fx-border-color: #333; -fx-border-width: 1;");
                                iv.setOnMouseClicked(e -> loadMainImage(p));
                                detThumbnails.getChildren().add(iv);
                        }
                } catch (Exception e) {
                }
        }

        private void loadMainImage(String p) {
                try {
                        File f = new File(p);
                        if (f.exists())
                                detMainImage.setImage(new Image(f.toURI().toString()));
                } catch (Exception e) {
                }
        }

        @FXML
        private void handleAddVehicle() {
                openDialog("/view/AddVehicleView.fxml", "Add New Vehicle");
        }

        @FXML
        private void handleEditVehicle() {
                Vehicle selected = vehicleTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                        try {
                                FXMLLoader loader = new FXMLLoader(
                                                getClass().getResource("/view/EditVehicleView.fxml"));
                                Parent root = loader.load();
                                EditVehicleController c = loader.getController();
                                c.setVehicle(selected);
                                c.setOnSaveSuccess(this::refreshTable);
                                showStage(root, "Edit Vehicle");
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                } else {
                        showAlert("No Selection", "Please select a vehicle to edit.");
                }
        }

        @FXML
        private void handleEditFromDetails() {
                handleEditVehicle();
                Vehicle s = vehicleTable.getSelectionModel().getSelectedItem();
                if (s != null)
                        populateDetails(s);
        }

        @FXML
        private void handleDeleteVehicle() {
                Vehicle s = vehicleTable.getSelectionModel().getSelectedItem();
                if (s != null) {
                        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + s.getBrand() + "?",
                                        ButtonType.YES, ButtonType.NO);
                        if (a.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                                vehicleDAO.deleteVehicle(s);
                                refreshTable();
                        }
                }
        }

        private void openDialog(String fxml, String title) {
                try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
                        Parent root = loader.load();
                        if (loader.getController() instanceof AddVehicleController c)
                                c.setOnSaveSuccess(this::refreshTable);
                        showStage(root, title);
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        private void showStage(Parent root, String title) {
                Stage s = new Stage();
                s.setTitle(title);
                s.setScene(new Scene(root));
                s.initModality(Modality.APPLICATION_MODAL);
                s.initOwner(vehicleTable.getScene().getWindow());
                s.getScene().getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
                s.showAndWait();
        }

        @FXML
        private void showAll() {
                handleBackToList();
                refreshTable();
        }

        @FXML
        private void showCars() {
                handleBackToList();
                filter(Car.class);
        }

        @FXML
        private void showMotorcycles() {
                handleBackToList();
                filter(Motorcycle.class);
        }

        @FXML
        private void showTrucks() {
                handleBackToList();
                filter(Truck.class);
        }

        private void filter(Class<? extends Vehicle> t) {
                vehicleList.setAll(vehicleDAO.getAllVehicles().stream().filter(t::isInstance).toList());
        }

        private void showAlert(String t, String c) {
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle(t);
                a.setContentText(c);
                a.showAndWait();
        }
}
