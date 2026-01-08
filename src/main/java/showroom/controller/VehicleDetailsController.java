package showroom.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.ColumnConstraints;
import javafx.stage.Stage;
import showroom.model.Car;
import showroom.model.Motorcycle;
import showroom.model.Truck;
import showroom.model.Vehicle;

import java.io.File;
import java.util.List;

public class VehicleDetailsController {

    @FXML
    private ImageView mainImageView;
    @FXML
    private Label noImageLabel;
    @FXML
    private HBox thumbnailContainer;
    @FXML
    private Label statusLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private Label titleText;
    @FXML
    private Label yearText;
    @FXML
    private Label priceLabel;
    @FXML
    private Label colorLabel;
    @FXML
    private Label idLabel;
    @FXML
    private VBox specificationsContainer;
    @FXML
    private VBox conditionSection;
    @FXML
    private VBox conditionContainer;

    private Vehicle vehicle;
    private Runnable onEditRequest;

    @FXML
    public void initialize() {
        // Initialization if needed
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        try {
            populateDetails();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnEditRequest(Runnable onEditRequest) {
        this.onEditRequest = onEditRequest;
    }

    private void populateDetails() {
        if (vehicle == null)
            return;

        titleText.setText(vehicle.getBrand() + " " + vehicle.getModel());
        yearText.setText(String.valueOf(vehicle.getYear()));
        priceLabel.setText(String.format("$%,.2f", vehicle.getPrice()));
        colorLabel.setText(vehicle.getColor());
        idLabel.setText("#" + vehicle.getId());
        statusLabel.setText(vehicle.getStatus());
        typeLabel.setText(vehicle.getClass().getSimpleName().toUpperCase());

        // Status badge styling
        statusLabel.getStyleClass().clear();
        if ("AVAILABLE".equals(vehicle.getStatus())) {
            statusLabel.getStyleClass().add("status-badge-available");
        } else {
            statusLabel.getStyleClass().add("status-badge-sold");
        }

        // Populate specifications based on vehicle type
        populateSpecifications();

        // Images
        thumbnailContainer.getChildren().clear();
        List<String> images = vehicle.getImages();
        if (images != null && !images.isEmpty()) {
            noImageLabel.setVisible(false);
            mainImageView.setVisible(true);
            loadMainImage(images.get(0));
            for (String path : images) {
                addThumbnail(path);
            }
        } else {
            noImageLabel.setVisible(true);
            mainImageView.setVisible(false);
            mainImageView.setImage(null);
        }
    }

    private void populateSpecifications() {
        specificationsContainer.getChildren().clear();
        conditionContainer.getChildren().clear();

        if (vehicle instanceof Car) {
            Car car = (Car) vehicle;

            // Show condition section for cars
            conditionSection.setVisible(true);
            conditionSection.setManaged(true);

            // Populate condition information
            GridPane conditionGrid = new GridPane();
            conditionGrid.setHgap(20);
            conditionGrid.setVgap(10);
            conditionGrid.getColumnConstraints().addAll(
                    new ColumnConstraints(120),
                    new ColumnConstraints());

            addGridSpec(conditionGrid, 0, "Condition:",
                    car.getCondition() != null ? car.getCondition() : "N/A",
                    "Used".equals(car.getCondition()) ? "#FF9800" : "#4CAF50");

            if (car.getMileage() != null && car.getMileage() > 0) {
                addGridSpec(conditionGrid, 1, "Mileage:",
                        String.format("%,d miles", car.getMileage()), "#EEE");
            }

            conditionContainer.getChildren().add(conditionGrid);

            // Populate technical specifications
            GridPane techGrid = new GridPane();
            techGrid.setHgap(20);
            techGrid.setVgap(10);
            techGrid.getColumnConstraints().addAll(
                    new ColumnConstraints(120),
                    new ColumnConstraints());

            int row = 0;
            addGridSpec(techGrid, row++, "Doors:", String.valueOf(car.getNumberOfDoors()), "#EEE");
            addGridSpec(techGrid, row++, "Transmission:",
                    car.getTransmission() != null ? car.getTransmission() : "N/A", "#EEE");
            addGridSpec(techGrid, row++, "Engine:",
                    car.getEngine() != null ? car.getEngine() : "N/A", "#EEE");
            addGridSpec(techGrid, row++, "Fuel Type:",
                    car.getFuelType() != null ? car.getFuelType() : "N/A", "#EEE");
            addGridSpec(techGrid, row++, "Drivetrain:",
                    car.getDrivetrainType() != null ? car.getDrivetrainType() : "N/A", "#EEE");

            specificationsContainer.getChildren().add(techGrid);

        } else {
            // Hide condition section for non-cars
            conditionSection.setVisible(false);
            conditionSection.setManaged(false);

            GridPane techGrid = new GridPane();
            techGrid.setHgap(20);
            techGrid.setVgap(10);
            techGrid.getColumnConstraints().addAll(
                    new ColumnConstraints(120),
                    new ColumnConstraints());

            if (vehicle instanceof Motorcycle) {
                Motorcycle moto = (Motorcycle) vehicle;
                addGridSpec(techGrid, 0, "Engine Capacity:", moto.getEngineCapacity() + " cc", "#EEE");
                addGridSpec(techGrid, 1, "Sidecar:", moto.isHasSidecar() ? "Yes" : "No", "#EEE");
            } else if (vehicle instanceof Truck) {
                Truck truck = (Truck) vehicle;
                addGridSpec(techGrid, 0, "Load Capacity:",
                        String.format("%,.0f kg", truck.getLoadCapacity()), "#EEE");
                addGridSpec(techGrid, 1, "Number of Axles:", String.valueOf(truck.getNumberOfAxles()), "#EEE");
            }

            specificationsContainer.getChildren().add(techGrid);
        }
    }

    private void addGridSpec(GridPane grid, int row, String key, String value, String valueColor) {
        Label keyLabel = new Label(key);
        keyLabel.setStyle("-fx-text-fill: #999; -fx-font-size: 13px;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-text-fill: " + valueColor + "; -fx-font-weight: bold; -fx-font-size: 13px;");

        GridPane.setConstraints(keyLabel, 0, row);
        GridPane.setConstraints(valueLabel, 1, row);

        grid.getChildren().addAll(keyLabel, valueLabel);
    }

    private void addThumbnail(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                Image img = new Image(file.toURI().toString(), 80, 80, true, true);
                ImageView thumbView = new ImageView(img);
                thumbView.setFitWidth(80);
                thumbView.setFitHeight(60);
                thumbView.setPreserveRatio(true);
                thumbView.setStyle("-fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 5, 0, 0, 2);");
                thumbView.setOnMouseClicked(e -> loadMainImage(path));
                thumbnailContainer.getChildren().add(thumbView);
            }
        } catch (Exception e) {
            System.err.println("Error loading thumbnail: " + path);
        }
    }

    private void loadMainImage(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                Image img = new Image(file.toURI().toString());
                mainImageView.setImage(img);
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + path);
        }
    }

    @FXML
    private void handleClose() {
        if (mainImageView.getScene() != null && mainImageView.getScene().getWindow() != null) {
            ((Stage) mainImageView.getScene().getWindow()).close();
        }
    }

    @FXML
    private void handleEdit() {
        if (onEditRequest != null) {
            handleClose();
            onEditRequest.run();
        }
    }
}
