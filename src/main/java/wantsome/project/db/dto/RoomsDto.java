package wantsome.project.db.dto;

import java.util.Objects;

public class RoomsDto {

    private final long id;
    private final String roomType;
    private final String extraInfo;

    public RoomsDto(long id, String roomType, String extraInfo) {
        this.id = id;
        this.roomType = roomType;
        this.extraInfo = extraInfo;
    }

    public long getId() {
        return id;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomsDto roomsDto = (RoomsDto) o;
        return id == roomsDto.id &&
                Objects.equals(roomType, roomsDto.roomType) &&
                Objects.equals(extraInfo, roomsDto.extraInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roomType, extraInfo);
    }

    @Override
    public String toString() {
        return "RoomsDto{" +
                "id=" + id +
                ", roomType='" + roomType + '\'' +
                ", extraInfo='" + extraInfo + '\'' +
                '}';
    }
}
