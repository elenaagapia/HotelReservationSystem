package wantsome.project.db.dto;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

public class ReservationDto {

    private final long id;
    private final long clientId;
    private final Date startDate;
    private final Date endDate;
    private final long roomNumber;
    private final String extraInfo;
    private final PaymentMethod payment;
    private final Date createdAt;

    //Constructor for when a particular reservation already exists and has a creation date and I don't want to replace the initial one
    public ReservationDto(long id, long clientId, Date startDate, Date endDate,
                          long roomNumber, String extraInfo, PaymentMethod payment, Date createdAt) {
        this.id = id;
        this.clientId = clientId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.roomNumber = roomNumber;
        this.extraInfo = extraInfo;
        this.payment = payment;
        this.createdAt = createdAt;
    }

    //Constructor for when a new reservation is created and I want the accurate date without inserting it
    public ReservationDto(long id, long clientId, Date startDate, Date endDate,
                          long roomNumber, String extraInfo, PaymentMethod payment) {
        this(id, clientId, startDate, endDate, roomNumber, extraInfo, payment, Date.valueOf(LocalDate.now()));
    }


    public long getId() {
        return id;
    }

    public long getClientId() {
        return clientId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public long getRoomNumber() {
        return roomNumber;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public PaymentMethod getPayment() {
        return payment;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationDto that = (ReservationDto) o;
        return id == that.id &&
                clientId == that.clientId &&
                roomNumber == that.roomNumber &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(extraInfo, that.extraInfo) &&
                Objects.equals(payment, that.payment) &&
                Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientId, startDate, endDate, roomNumber, extraInfo, payment, createdAt);
    }

    @Override
    public String toString() {
        return "ReservationDto{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", roomNumber=" + roomNumber +
                ", extraInfo='" + extraInfo + '\'' +
                ", payment=" + payment +
                ", createdAt=" + createdAt +
                '}';
    }
}