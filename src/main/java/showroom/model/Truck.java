package showroom.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "trucks")
public class Truck extends Vehicle {
    private double loadCapacity;
    private int numberOfAxles;

    public Truck() {
    }

    public Truck(String brand, String model, int year, double price, String color, double loadCapacity,
            int numberOfAxles) {
        super(brand, model, year, price, color);
        this.loadCapacity = loadCapacity;
        this.numberOfAxles = numberOfAxles;
    }

    public Truck(String brand, String model, int year, double price, String color, String status,
            java.util.List<String> images, double loadCapacity,
            int numberOfAxles) {
        super(brand, model, year, price, color, status, images);
        this.loadCapacity = loadCapacity;
        this.numberOfAxles = numberOfAxles;
    }

    public double getLoadCapacity() {
        return loadCapacity;
    }

    public void setLoadCapacity(double loadCapacity) {
        this.loadCapacity = loadCapacity;
    }

    public int getNumberOfAxles() {
        return numberOfAxles;
    }

    public void setNumberOfAxles(int numberOfAxles) {
        this.numberOfAxles = numberOfAxles;
    }
}
