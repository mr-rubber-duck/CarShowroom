package showroom.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import showroom.dao.VehicleDAO;
import showroom.model.Car;
import showroom.model.Motorcycle;
import showroom.model.Truck;
import showroom.model.Vehicle;
import showroom.util.ImageService;

public class EditVehicleController {

    @FXML
    private Text vehicleIdText;
    @FXML
    private TextField typeField;
    @FXML
    private TextField brandField;
    @FXML
    private TextField modelField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField colorField;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private Label imageCountLabel;
    @FXML
    private VBox specificFieldsContainer;

    private java.util.List<String> selectedImagePaths = new java.util.ArrayList<>();

    // Car specific fields
    private TextField doorsField;
    private ComboBox<String> fuelTypeComboBox;
    private ComboBox<String> conditionComboBox;
    private TextField mileageField;
    private ComboBox<String> transmissionComboBox;
    private TextField engineField;
    private ComboBox<String> drivetrainComboBox;

    // Motorcycle specific fields
    private TextField engineCapacityField;
    private CheckBox hasSidecarBox;

    // Truck specific fields
    private TextField loadCapacityField;
    private TextField axlesField;

    private final VehicleDAO vehicleDAO = new VehicleDAO();
    private Runnable onSaveSuccess;
    private Vehicle currentVehicle;

    public void setVehicle(Vehicle vehicle) {
        this.currentVehicle = vehicle;
        populateFields();
    }

    public void setOnSaveSuccess(Runnable onSaveSuccess) {
        this.onSaveSuccess = onSaveSuccess;
    }

    private void populateFields() {
        if (currentVehicle == null)
            return;

        vehicleIdText.setText("Editing Vehicle ID: " + currentVehicle.getId());
        typeField.setText(currentVehicle.getClass().getSimpleName());
        brandField.setText(currentVehicle.getBrand());
        modelField.setText(currentVehicle.getModel());
        yearField.setText(String.valueOf(currentVehicle.getYear()));
        priceField.setText(String.valueOf(currentVehicle.getPrice()));
        colorField.setText(currentVehicle.getColor());
        statusComboBox.getSelectionModel().select(currentVehicle.getStatus());

        selectedImagePaths = new java.util.ArrayList<>(currentVehicle.getImages());
        imageCountLabel.setText(selectedImagePaths.size() + " images selected");

        initializeSpecificFields(currentVehicle);
    }

    private void initializeSpecificFields(Vehicle vehicle) {
        specificFieldsContainer.getChildren().clear();
        GridPane gp = new GridPane();
        gp.setHgap(10);
        gp.setVgap(10);
        gp.getColumnConstraints().add(new javafx.scene.layout.ColumnConstraints(120));

        if (vehicle instanceof Car) {
            Car car = (Car) vehicle;

            doorsField = new TextField(String.valueOf(car.getNumberOfDoors()));

            fuelTypeComboBox = new ComboBox<>();
            fuelTypeComboBox.getItems().addAll("Gasoline", "Diesel", "Electric", "Hybrid", "Plug-in Hybrid");
            fuelTypeComboBox.setMaxWidth(Double.MAX_VALUE);
            fuelTypeComboBox.getSelectionModel().select(car.getFuelType());

            conditionComboBox = new ComboBox<>();
            conditionComboBox.getItems().addAll("New", "Used");
            conditionComboBox.setMaxWidth(Double.MAX_VALUE);
            conditionComboBox.getSelectionModel().select(car.getCondition());

            mileageField = new TextField();
            if (car.getMileage() != null) {
                mileageField.setText(String.valueOf(car.getMileage()));
            }
            mileageField.setPromptText("e.g. 15000");

            transmissionComboBox = new ComboBox<>();
            transmissionComboBox.getItems().addAll("Automatic", "Manual", "CVT", "Semi-Automatic", "Dual-Clutch");
            transmissionComboBox.setMaxWidth(Double.MAX_VALUE);
            transmissionComboBox.getSelectionModel().select(car.getTransmission());

            engineField = new TextField(car.getEngine() != null ? car.getEngine() : "");
            engineField.setPromptText("e.g. 2.0L Turbo I4");

            drivetrainComboBox = new ComboBox<>();
            drivetrainComboBox.getItems().addAll("FWD", "RWD", "AWD", "4WD");
            drivetrainComboBox.setMaxWidth(Double.MAX_VALUE);
            drivetrainComboBox.getSelectionModel().select(car.getDrivetrainType());

            // Enable/disable mileage based on condition
            if ("Used".equals(car.getCondition())) {
                mileageField.setDisable(false);
            } else {
                mileageField.setDisable(true);
            }

            // Add listener to enable/disable mileage field based on condition
            conditionComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if ("Used".equals(newVal)) {
                    mileageField.setDisable(false);
                } else {
                    mileageField.setDisable(true);
                    mileageField.clear();
                }
            });

            gp.addRow(0, new Label("Doors:"), doorsField);
            gp.addRow(1, new Label("Condition:"), conditionComboBox);
            gp.addRow(2, new Label("Mileage:"), mileageField);
            gp.addRow(3, new Label("Transmission:"), transmissionComboBox);
            gp.addRow(4, new Label("Engine:"), engineField);
            gp.addRow(5, new Label("Fuel Type:"), fuelTypeComboBox);
            gp.addRow(6, new Label("Drivetrain:"), drivetrainComboBox);

        } else if (vehicle instanceof Motorcycle) {
            Motorcycle moto = (Motorcycle) vehicle;
            engineCapacityField = new TextField(String.valueOf(moto.getEngineCapacity()));
            hasSidecarBox = new CheckBox("Has Sidecar");
            hasSidecarBox.setSelected(moto.isHasSidecar());

            gp.addRow(0, new Label("Engine (cc):"), engineCapacityField);
            gp.addRow(1, new Label("Sidecar:"), hasSidecarBox);

        } else if (vehicle instanceof Truck) {
            Truck truck = (Truck) vehicle;
            loadCapacityField = new TextField(String.valueOf(truck.getLoadCapacity()));
            axlesField = new TextField(String.valueOf(truck.getNumberOfAxles()));

            gp.addRow(0, new Label("Load (kg):"), loadCapacityField);
            gp.addRow(1, new Label("Axles:"), axlesField);
        }

        specificFieldsContainer.getChildren().add(gp);
    }

    @FXML
    private void handleSelectImages() {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Select Vehicle Images");
        fileChooser.getExtensionFilters().addAll(
                new javafx.stage.FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        java.util.List<java.io.File> selectedFiles = fileChooser
                .showOpenMultipleDialog(brandField.getScene().getWindow());
        if (selectedFiles != null) {
            selectedImagePaths.clear();
            for (java.io.File file : selectedFiles) {
                selectedImagePaths.add(file.getAbsolutePath());
            }
            imageCountLabel.setText(selectedImagePaths.size() + " images selected");
        }
    }

    @FXML
    private void handleSave() {
        try {
            // Update common fields
            currentVehicle.setBrand(brandField.getText());
            currentVehicle.setModel(modelField.getText());
            currentVehicle.setColor(colorField.getText());
            currentVehicle.setYear(Integer.parseInt(yearField.getText()));
            currentVehicle.setPrice(Double.parseDouble(priceField.getText()));
            currentVehicle.setStatus(statusComboBox.getValue());

            // Store images locally first
            java.util.List<String> storedImages = ImageService.storeImages(selectedImagePaths);
            currentVehicle.setImages(new java.util.ArrayList<>(storedImages));

            // Update specific fields
            if (currentVehicle instanceof Car) {
                Car car = (Car) currentVehicle;

                // Validate required fields
                if (doorsField.getText().isEmpty()) {
                    showAlert("Error", "Please enter the number of doors.");
                    return;
                }
                if (conditionComboBox.getValue() == null) {
                    showAlert("Error", "Please select the vehicle condition.");
                    return;
                }
                if (transmissionComboBox.getValue() == null) {
                    showAlert("Error", "Please select the transmission type.");
                    return;
                }
                if (engineField.getText().isEmpty()) {
                    showAlert("Error", "Please enter the engine details.");
                    return;
                }
                if (fuelTypeComboBox.getValue() == null) {
                    showAlert("Error", "Please select the fuel type.");
                    return;
                }
                if (drivetrainComboBox.getValue() == null) {
                    showAlert("Error", "Please select the drivetrain type.");
                    return;
                }

                car.setNumberOfDoors(Integer.parseInt(doorsField.getText()));
                car.setFuelType(fuelTypeComboBox.getValue());
                car.setCondition(conditionComboBox.getValue());

                // Handle mileage for used cars
                if ("Used".equals(conditionComboBox.getValue())) {
                    if (mileageField.getText().isEmpty()) {
                        showAlert("Error", "Please enter the mileage for a used car.");
                        return;
                    }
                    car.setMileage(Integer.parseInt(mileageField.getText()));
                } else {
                    car.setMileage(null);
                }

                car.setTransmission(transmissionComboBox.getValue());
                car.setEngine(engineField.getText());
                car.setDrivetrainType(drivetrainComboBox.getValue());

            } else if (currentVehicle instanceof Motorcycle) {
                Motorcycle moto = (Motorcycle) currentVehicle;
                if (engineCapacityField.getText().isEmpty()) {
                    showAlert("Error", "Please enter the engine capacity.");
                    return;
                }
                moto.setEngineCapacity(Integer.parseInt(engineCapacityField.getText()));
                moto.setHasSidecar(hasSidecarBox.isSelected());

            } else if (currentVehicle instanceof Truck) {
                Truck truck = (Truck) currentVehicle;
                if (loadCapacityField.getText().isEmpty()) {
                    showAlert("Error", "Please enter the load capacity.");
                    return;
                }
                if (axlesField.getText().isEmpty()) {
                    showAlert("Error", "Please enter the number of axles.");
                    return;
                }
                truck.setLoadCapacity(Double.parseDouble(loadCapacityField.getText()));
                truck.setNumberOfAxles(Integer.parseInt(axlesField.getText()));
            }

            vehicleDAO.updateVehicle(currentVehicle);

            if (onSaveSuccess != null) {
                onSaveSuccess.run();
            }
            closeWindow();

        } catch (NumberFormatException e) {
            showAlert("Format Error", "Please ensure numbers are valid.");
        } catch (Exception e) {
            showAlert("Error", "Could not update vehicle: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) brandField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
