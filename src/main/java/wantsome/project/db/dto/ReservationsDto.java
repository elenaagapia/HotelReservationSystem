package wantsome.project.db.dto;

import java.sql.Date;
import java.util.Objects;

public class ReservationsDto {

    private final long id;
    private final long clientId;
    private final Date startDate;
    private final Date endDate;
    private final long roomId;
    private final String extraInfo;
    private final long extraFacilities_Id;
    private final String payment;

    public ReservationsDto(long id, long clientId, Date startDate, Date endDate, long roomId, String extraInfo, long extraFacilities_Id, String payment) {
        this.id = id;
        this.clientId = clientId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.roomId = roomId;
        this.extraInfo = extraInfo;
        this.extraFacilities_Id = extraFacilities_Id ;
        this.payment = payment;
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

    public long getExtraFacilities_Id () {

        return extraFacilities_Id;
    }

    public String getPayment() {
        return payment;
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
                ", extraFacilities_Id=" + extraFacilities_Id +
                ", payment='" + payment + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationsDto that = (ReservationsDto) o;
        return id == that.id &&
                clientId == that.clientId &&
                roomId == that.roomId &&
                extraFacilities_Id == that.extraFacilities_Id &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(extraInfo, that.extraInfo) &&
                Objects.equals(payment, that.payment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientId, startDate, endDate, roomId, extraInfo, extraFacilities_Id, payment);
    }
}
