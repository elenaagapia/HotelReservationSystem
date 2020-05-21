package wantsome.project.db.dto;

import java.util.Objects;

public class Extra_facilitiesDto {

    private final ExtraServices facility;
    private final double price;


    public ExtraServices getFacility() {
        return facility;
    }

    public double getPrice() {
        return price;
    }

    public Extra_facilitiesDto(ExtraServices facility, double price) {
        this.facility = facility;
        this.price = price;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Extra_facilitiesDto that = (Extra_facilitiesDto) o;
        return Double.compare(that.price, price) == 0 &&
                facility == that.facility;
    }

    @Override
    public int hashCode() {
        return Objects.hash(facility, price);
    }

    @Override
    public String toString() {
        return "Extra_facilitiesDto{" +
                "facility=" + facility +
                ", price=" + price +
                '}';
    }
}
