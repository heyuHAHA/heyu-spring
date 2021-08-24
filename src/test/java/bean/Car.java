package bean;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Value;

@Component
public class Car {
    @Value("${brand}")
    private String brand;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public String toString() {
        return "Car {brand= " +brand +  " }";
    }
}
