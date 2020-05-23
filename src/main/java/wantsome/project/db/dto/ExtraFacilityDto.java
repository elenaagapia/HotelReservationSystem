package wantsome.project.db.dto;

import java.util.Objects;

public class ExtraFacilityDto {

    private final ExtraServices facility;
    private final double price;


    public ExtraFacilityDto(ExtraServices facility, double price) {
        this.facility = facility;
        this.price = price;

    }

    public ExtraServices getFacility() {
        return facility;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtraFacilityDto that = (ExtraFacilityDto) o;
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
