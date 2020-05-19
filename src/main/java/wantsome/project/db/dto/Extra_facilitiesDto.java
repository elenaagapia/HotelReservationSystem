package wantsome.project.db.dto;

import java.util.Objects;

public class Extra_facilitiesDto {

    private final long id;
    private final String description;
    private final double price;
    private final ExtraFacilities facility;

    public Extra_facilitiesDto(long id, String description, double price, ExtraFacilities facility) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.facility = facility;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public ExtraFacilities getFacility() {
        return facility;
    }

    public Extra_facilitiesDto(String description, double price, ExtraFacilities facility) {
        this(-1, description, price, facility);


    }

    @Override
    public String toString() {
        return "Extra_facilitiesDto{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", facility=" + facility +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Extra_facilitiesDto that = (Extra_facilitiesDto) o;
        return id == that.id &&
                Double.compare(that.price, price) == 0 &&
                Objects.equals(description, that.description) &&
                facility == that.facility;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, price, facility);
    }
}
