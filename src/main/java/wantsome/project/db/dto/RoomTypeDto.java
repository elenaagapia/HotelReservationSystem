package wantsome.project.db.dto;

import java.util.Objects;

public class RoomTypeDto {

    private final RoomTypes type;
    private final double price;
    private final int capacity;

    public RoomTypeDto(RoomTypes type, double price, int capacity) {
        this.type = type;
        this.price = price;
        this.capacity = capacity;
    }

    public RoomTypes getType() {
        return type;
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
        RoomTypeDto that = (RoomTypeDto) o;
        return Double.compare(that.price, price) == 0 &&
                capacity == that.capacity &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, price, capacity);
    }

    @Override
    public String toString() {
        return "RoomTypeDto{" +
                "type=" + type +
                ", price=" + price +
                ", capacity=" + capacity +
                '}';
    }
}
