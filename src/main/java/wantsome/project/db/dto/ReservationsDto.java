package wantsome.project.db.dto;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

public class ReservationsDto {

    private final long id;
    private final long clientId;
    private final Date startDate;
    private final Date endDate;
    private final long roomId;
    private final String extraInfo;
    private final String extraFacilities;
    private final String payment;
    private final Date createdAt;

    //Constructor for when a particular reservation already exists and has a creation date and I don't want to replace the initial one
    public ReservationsDto(long id, long clientId, Date startDate, Date endDate,
                           long roomId, String extraInfo, String extraFacilities, String payment, Date createdAt) {
        this.id = id;
        this.clientId = clientId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.roomId = roomId;
        this.extraInfo = extraInfo;
        this.extraFacilities = extraFacilities;
        this.payment = payment;
        this.createdAt = createdAt;
    }

    //Constructor for when a new reservation is created and I want the accurate date without inserting it
    public ReservationsDto(long id, long clientId, Date startDate, Date endDate,
                           long roomId, String extraInfo, String extraFacilities, String payment) {
        this(id, clientId, startDate, endDate, roomId, extraInfo, extraFacilities, payment, Date.valueOf(LocalDate.now()));
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

    public long getRoomId() {
        return roomId;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public String getExtraFacilities() {

        return extraFacilities;
    }

    public String getPayment() {
        return payment;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationsDto that = (ReservationsDto) o;
        return id == that.id &&
                clientId == that.clientId &&
                roomId == that.roomId &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(extraInfo, that.extraInfo) &&
                Objects.equals(extraFacilities, that.extraFacilities) &&
                Objects.equals(payment, that.payment) &&
                Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientId, startDate, endDate, roomId, extraInfo, extraFacilities, payment, createdAt);
    }

    @Override
    public String toString() {
        return "ReservationsDto{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", roomId=" + roomId +
                ", extraInfo='" + extraInfo + '\'' +
                ", extraFacilities='" + extraFacilities + '\'' +
                ", payment='" + payment + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}