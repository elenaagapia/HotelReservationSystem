package wantsome.project;

import wantsome.project.db.dto.*;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static wantsome.project.db.dto.ExtraServices.*;
import static wantsome.project.db.dto.PaymentMethod.CARD;

public class SampleItems {

     static final List<ClientDto> clients = Arrays.asList(
             new ClientDto("Andrei Vieriu", "andrei.aa@gmail.com", "Iasi, Sos. Nicolina"),
             new ClientDto("Bianca Nedelcu", "nedelcu.bianca@yahoo.com", "Bucuresti, str. Aeroportului"),
             new ClientDto("Cristina Macovei", "criss.macovei@gmail.com", "Iasi, Blvd. Stefan cel Mare")
     );

    static final List<ReservationDto> reservations = Arrays.asList(
            new ReservationDto(-1, 1, Date.valueOf(String.valueOf(2020 - 4 - 20)), Date.valueOf(String.valueOf(2020 - 4 - 30)), 1,
                    "regular customer", Arrays.asList(BREAKFAST), CARD),
            new ReservationDto(-1, 2, new Date(2020 - 6 - 15),
                    Date.valueOf(String.valueOf(2020 - 6 - 22)), 4, "italian family",
                    Arrays.asList(BREAKFAST, PARKING_SPACE, SPA_MEMBERSHIP),
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