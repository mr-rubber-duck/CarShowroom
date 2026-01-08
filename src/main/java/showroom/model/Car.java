package showroom.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "cars")
public class Car extends Vehicle {
    private int numberOfDoors;
    private String fuelType;
    private String condition;
    private Integer mileage;
    private String transmission;
    private String engine;
    private String drivetrainType;

    public Car() {
    }

    public Car(String brand, String model, int year, double price, String color, int numberOfDoors, String fuelType) {
        super(brand, model, year, price, color);
        this.numberOfDoors = numberOfDoors;
        this.fuelType = fuelType;
    }

    public Car(String brand, String model, int year, double price, String color, String status,
            java.util.List<String> images, int numberOfDoors, String fuelType, String condition,
            Integer mileage, String transmission, String engine, String drivetrainType) {
        super(brand, model, year, price, color, status, images);
        this.numberOfDoors = numberOfDoors;
        this.fuelType = fuelType;
        this.condition = condition;
        this.mileage = mileage;
        this.transmission = transmission;
        this.engine = engine;
        this.drivetrainType = drivetrainType;
    }

    // Getters and Setters
    public int getNumberOfDoors() {
        return numberOfDoors;
    }

    public void setNumberOfDoors(int numberOfDoors) {
        this.numberOfDoors = numberOfDoors;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getDrivetrainType() {
        return drivetrainType;
    }

    public void setDrivetrainType(String drivetrainType) {
        this.drivetrainType = drivetrainType;
    }
}
