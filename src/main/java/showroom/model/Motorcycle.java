package showroom.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "motorcycles")
public class Motorcycle extends Vehicle {
    private int engineCapacity;
    private boolean hasSidecar;

    public Motorcycle() {
    }

    public Motorcycle(String brand, String model, int year, double price, String color, int engineCapacity,
            boolean hasSidecar) {
        super(brand, model, year, price, color);
        this.engineCapacity = engineCapacity;
        this.hasSidecar = hasSidecar;
    }

    public Motorcycle(String brand, String model, int year, double price, String color, String status,
            java.util.List<String> images, int engineCapacity,
            boolean hasSidecar) {
        super(brand, model, year, price, color, status, images);
        this.engineCapacity = engineCapacity;
        this.hasSidecar = hasSidecar;
    }

    public int getEngineCapacity() {
        return engineCapacity;
    }

    public void setEngineCapacity(int engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    public boolean isHasSidecar() {
        return hasSidecar;
    }

    public void setHasSidecar(boolean hasSidecar) {
        this.hasSidecar = hasSidecar;
    }
}
