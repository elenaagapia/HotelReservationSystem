package wantsome.project.db.dto;

import java.util.Objects;

public class Room_typesDto {

    private final String description;
    private final double price;
    private final int capacity;

    public Room_typesDto(String description, double price, int capacity) {
        this.description = description;
        this.price = price;
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room_typesDto that = (Room_typesDto) o;
        return Double.compare(that.price, price) == 0 &&
                capacity == that.capacity &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, price, capacity);
    }

    @Override
    public String toString() {
        return "Room_typesDto{" +
                "description='" + description + '\'' +
                ", price=" + price +
                ", capacity=" + capacity +
                '}';
    }
}
