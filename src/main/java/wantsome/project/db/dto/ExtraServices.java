package wantsome.project.db.dto;

public enum ExtraServices {
    PARKING_SPACE(0.0),
    BREAKFAST(10.0),
    SPA_MEMBERSHIP(15.0),
    TRANSIT_TRANSPORTATION(10.0),
    DAY_CARE(20.0);

    private final double pricePerDay;

    ExtraServices(double price) {
        this.pricePerDay = price;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }
}
