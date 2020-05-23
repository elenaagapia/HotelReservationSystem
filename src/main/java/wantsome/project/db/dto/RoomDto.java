package wantsome.project.db.dto;

import java.util.Objects;

public class RoomDto {

    private final long number;
    private final RoomTypes roomType;
    private final String extraInfo;

    public RoomDto(long number, RoomTypes roomType, String extraInfo) {
        this.number = number;
        this.roomType = roomType;
        this.extraInfo = extraInfo;
    }

    public long getNumber() {
        return number;
    }

    public RoomTypes getRoomType() {
        return roomType;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomDto roomDto = (RoomDto) o;
        return number == roomDto.number &&
                roomType == roomDto.roomType &&
                Objects.equals(extraInfo, roomDto.extraInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, roomType, extraInfo);
    }

    @Override
    public String toString() {
        return "RoomDto{" +
                "number=" + number +
                ", roomType=" + roomType +
                ", extraInfo='" + extraInfo + '\'' +
                '}';
    }
}
