package wantsome.project;

import wantsome.project.db.dto.*;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static wantsome.project.db.dto.PaymentMethod.CARD;

public class SampleItems {
    private static final long baseTime = System.currentTimeMillis();
    static final List<ClientDto> clients = Arrays.asList(
            new ClientDto(-1, "Andrei Vieriu", "andrei.aa@gmail.com", "Iasi, Sos. Nicolina"),
            new ClientDto(-1, "Bianca Nedelcu", "nedelcu.bianca@yahoo.com", "Bucuresti, str. Aeroportului"),
            new ClientDto(-1, "Cristina Macovei", "criss.macovei@gmail.com", "Iasi, Blvd. Stefan cel Mare")
    );

    static final List<ReservationDto> reservations = Arrays.asList(
            new ReservationDto(-1, 1, new Date(baseTime), new Date(baseTime + 1000), 1,
                    "regular customer", /*Arrays.asList(BREAKFAST),*/ CARD),
            new ReservationDto(-1, 2, new Date(baseTime + 2000),
                    new Date(baseTime + 3000), 4, "italian family",
                    /*Arrays.asList(BREAKFAST, PARKING_SPACE, SPA_MEMBERSHIP),*/
                    CARD));

    static final List<RoomDto> rooms = Arrays.asList(
            new RoomDto(-1, RoomTypes.DOUBLE, "sea view"),
            new RoomDto(-1, RoomTypes.TWIN, "near elevator"),
            new RoomDto(-1, RoomTypes.SINGLE, "sea view"),
            new RoomDto(-1, RoomTypes.APARTMENT, "two bathrooms access")
    );


    static final List<RoomTypeDto> roomTypes = Arrays.asList(
            new RoomTypeDto(RoomTypes.DOUBLE, 55.0, 2),
            new RoomTypeDto(RoomTypes.TWIN, 50, 2),
            new RoomTypeDto(RoomTypes.SINGLE, 30, 1),
            new RoomTypeDto(RoomTypes.APARTMENT, 100, 4)
    );

}