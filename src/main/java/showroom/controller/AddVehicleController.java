package showroom.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import showroom.dao.VehicleDAO;
import showroom.model.Car;
import showroom.model.Motorcycle;
import showroom.model.Truck;
import showroom.model.Vehicle;
import showroom.util.ImageService;

public class AddVehicleController {

    @FXML
    private ComboBox<String> typeComboBox;
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

    @FXML
    public void initialize() {
        initializeSpecificFields();

        // Default to Car and AVAILABLE
        typeComboBox.getSelectionModel().select("Car");
        statusComboBox.getSelectionModel().select("AVAILABLE");
        updateSpecificFields("Car");

        typeComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                updateSpecificFields(newVal);
            }
        });
    }

    public void setOnSaveSuccess(Runnable onSaveSuccess) {
        this.onSaveSuccess = onSaveSuccess;
    }

    private void initializeSpecificFields() {
        // Init Car fields
        doorsField = new TextField();
        doorsField.setPromptText("e.g. 4");

        fuelTypeComboBox = new ComboBox<>();
        fuelTypeComboBox.getItems().addAll("Gasoline", "Diesel", "Electric", "Hybrid", "Plug-in Hybrid");
        fuelTypeComboBox.setMaxWidth(Double.MAX_VALUE);

        conditionComboBox = new ComboBox<>();
        conditionComboBox.getItems().addAll("New", "Used");
        conditionComboBox.setMaxWidth(Double.MAX_VALUE);

        mileageField = new TextField();
        mileageField.setPromptText("e.g. 15000");
        mileageField.setDisable(true);

        transmissionComboBox = new ComboBox<>();
        transmissionComboBox.getItems().addAll("Automatic", "Manual", "CVT", "Semi-Automatic", "Dual-Clutch");
        transmissionComboBox.setMaxWidth(Double.MAX_VALUE);

        engineField = new TextField();
        engineField.setPromptText("e.g. 2.0L Turbo I4");

        drivetrainComboBox = new ComboBox<>();
        drivetrainComboBox.getItems().addAll("FWD", "RWD", "AWD", "4WD");
        drivetrainComboBox.setMaxWidth(Double.MAX_VALUE);

        // Init Motorcycle fields
        engineCapacityField = new TextField();
        engineCapacityField.setPromptText("e.g. 1000");
        hasSidecarBox = new CheckBox("Has Sidecar");

        // Init Truck fields
        loadCapacityField = new TextField();
        loadCapacityField.setPromptText("e.g. 20000.0");
        axlesField = new TextField();
        axlesField.setPromptText("e.g. 4");
    }

    private void updateSpecificFields(String type) {
        specificFieldsContainer.getChildren().clear();
        GridPane gp = new GridPane();
        gp.setHgap(10);
        gp.setVgap(10);
        gp.getColumnConstraints().add(new javafx.scene.layout.ColumnConstraints(120)); // Label width

        switch (type) {
            case "Car":
                // Re-initialize to ensure clean state if switching back and forth
                doorsField = new TextField();
                doorsField.setPromptText("e.g. 4");

                fuelTypeComboBox = new ComboBox<>();
                fuelTypeComboBox.getItems().addAll("Gasoline", "Diesel", "Electric", "Hybrid", "Plug-in Hybrid");
                fuelTypeComboBox.setMaxWidth(Double.MAX_VALUE);

                conditionComboBox = new ComboBox<>();
                conditionComboBox.getItems().addAll("New", "Used");
                conditionComboBox.setMaxWidth(Double.MAX_VALUE);

                mileageField = new TextField();
                mileageField.setPromptText("e.g. 15000");
                mileageField.setDisable(true);

                transmissionComboBox = new ComboBox<>();
                transmissionComboBox.getItems().addAll("Automatic", "Manual", "CVT", "Semi-Automatic", "Dual-Clutch");
                transmissionComboBox.setMaxWidth(Double.MAX_VALUE);

                engineField = new TextField();
                engineField.setPromptText("e.g. 2.0L Turbo I4");

                drivetrainComboBox = new ComboBox<>();
                drivetrainComboBox.getItems().addAll("FWD", "RWD", "AWD", "4WD");
                drivetrainComboBox.setMaxWidth(Double.MAX_VALUE);

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
                break;
            case "Motorcycle":
                engineCapacityField = new TextField();
                engineCapacityField.setPromptText("e.g. 1000");
                hasSidecarBox = new CheckBox("Has Sidecar");

                gp.addRow(0, new Label("Engine (cc):"), engineCapacityField);
                gp.addRow(1, new Label("Sidecar:"), hasSidecarBox);
                break;
            case "Truck":
                loadCapacityField = new TextField();
                loadCapacityField.setPromptText("e.g. 20000.0");
                axlesField = new TextField();
                axlesField.setPromptText("e.g. 4");

                gp.addRow(0, new Label("Load (kg):"), loadCapacityField);
                gp.addRow(1, new Label("Axles:"), axlesField);
                break;
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
                .showOpenMultipleDialog(typeComboBox.getScene().getWindow());
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
            String type = typeComboBox.getValue();
            String brand = brandField.getText();
            String model = modelField.getText();
            String color = colorField.getText();
            String status = statusComboBox.getValue();

            if (brand.isEmpty() || model.isEmpty() || yearField.getText().isEmpty() || priceField.getText().isEmpty()) {
                showAlert("Error", "Please fill in all common fields.");
                return;
            }

            int year = Integer.parseInt(yearField.getText());
            double price = Double.parseDouble(priceField.getText());

            // Store images locally first
            java.util.List<String> storedImages = ImageService.storeImages(selectedImagePaths);

            Vehicle vehicle = null;

            switch (type) {
                case "Car":
                    // Validate car-specific required fields
                    if (doorsField.getText().isEmpty()) {
                        showAlert("Error", "Please enter the number of doors.");
                        return;
                    }
                    if (conditionComboBox.getValue() == null) {
                        showAlert("Error", "Please select the vehicle condition (New/Used).");
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

                    int doors = Integer.parseInt(doorsField.getText());
                    String fuelType = fuelTypeComboBox.getValue();
                    String condition = conditionComboBox.getValue();
                    Integer mileage = null;

                    // Mileage is required for used cars
                    if ("Used".equals(condition)) {
                        if (mileageField.getText().isEmpty()) {
                            showAlert("Error", "Please enter the mileage for a used car.");
                            return;
                        }
                        mileage = Integer.parseInt(mileageField.getText());
                    }

                    String transmission = transmissionComboBox.getValue();
                    String engine = engineField.getText();
                    String drivetrain = drivetrainComboBox.getValue();

                    vehicle = new Car(brand, model, year, price, color, status, storedImages,
                            doors, fuelType, condition, mileage, transmission, engine, drivetrain);
                    break;

                case "Motorcycle":
                    if (engineCapacityField.getText().isEmpty()) {
                        showAlert("Error", "Please enter the engine capacity.");
                        return;
                    }
                    int engineCapacity = Integer.parseInt(engineCapacityField.getText());
                    boolean sidecar = hasSidecarBox.isSelected();
                    vehicle = new Motorcycle(brand, model, year, price, color, status, storedImages, engineCapacity,
                            sidecar);
                    break;

                case "Truck":
                    if (loadCapacityField.getText().isEmpty()) {
                        showAlert("Error", "Please enter the load capacity.");
                        return;
                    }
                    if (axlesField.getText().isEmpty()) {
                        showAlert("Error", "Please enter the number of axles.");
                        return;
                    }
                    double load = Double.parseDouble(loadCapacityField.getText());
                    int axles = Integer.parseInt(axlesField.getText());
                    vehicle = new Truck(brand, model, year, price, color, status, storedImages, load, axles);
                    break;
            }

            if (vehicle != null) {
                vehicleDAO.saveVehicle(vehicle);
                if (onSaveSuccess != null) {
                    onSaveSuccess.run();
                }
                closeWindow();
            }

        } catch (NumberFormatException e) {
            showAlert("Format Error", "Please ensure numbers (Year, Price, Doors, Mileage, etc.) are valid.");
        } catch (Exception e) {
            showAlert("Error", "Could not save vehicle: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) typeComboBox.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
