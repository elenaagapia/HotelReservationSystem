package wantsome.project;

<<<<<<< Updated upstream
import org.junit.Assert;
import org.junit.Test;

public class MainTest {

    //TODO: add unit tests here as needed (or else delete this class)
    @Test
    public void testSomething() {
        Assert.assertTrue(1 == 1);
=======
import org.junit.*;
import wantsome.project.db.DbManager;
import wantsome.project.db.dto.*;
import wantsome.project.db.service.*;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class MainTest {

    private static final String TEST_FILE = "final_project_test.db";

    private static final ClientDao clientDao = new ClientDao();
    private static final ReservationDao reservationDao = new ReservationDao();
    private static final RoomDao roomDao = new RoomDao();
    private static final RoomTypeDao roomTypeDao = new RoomTypeDao();
    private static final ExtraFacilityDao facilityDao = new ExtraFacilityDao();
    private static SampleItems sample = new SampleItems();

//    private static List<ExtraFacilityDto> facilitiesDto = Arrays.asList(new ExtraFacilityDto(BREAKFAST, 20.0),
//            new ExtraFacilityDto(SPA_MEMBERSHIP, 30.0));
//
//    private static final List<ClientDto> clients = Arrays.asList(
//            new ClientDto("Andrei Vieriu", "andrei.aa@gmail.com", "Iasi, Sos. Nicolina"),
//            new ClientDto("Bianca Nedelcu", "nedelcu.bianca@yahoo.com", "Bucuresti, str. Aeroportului"),
//            new ClientDto("Cristina Macovei", "criss.macovei@gmail.com", "Iasi, Blvd. Stefan cel Mare")
//    );
//    Date date = new Date(2020 - 10 - 10);
//    private static final List<ReservationDto> reservations = Arrays.asList(new ReservationDto(-1, 1, Date.valueOf(String.valueOf(2020 - 4 - 20)), Date.valueOf(String.valueOf(2020 - 4 - 30)), 4,
//                    "regular customer", facilitiesDto, CARD),
//            new ReservationDto(-1, 2, Date.valueOf(String.valueOf(2020-6-15)),
//                    Date.valueOf(String.valueOf(2020 - 6 - 22)), 7, "italian family",
//                    Arrays.asList(new ExtraFacilityDto(DAY_CARE, 30.0),
//                            new ExtraFacilityDto(TRANSIT_TRANSPORTATION, 15.0),
//                            new ExtraFacilityDto(PARKING_SPACE, 5.0)),
//                    CARD));


    @BeforeClass
    public static void initDbBeforeAnyTests() {//TODO:RENAME
        DbManager.setDbFile(TEST_FILE); //use a separate db for test, to avoid overwriting the real one
        DbInitService.createTablesAndInitialData();
    }

    @Before
    public void insertBeforeTest() {
        assertTrue(clientDao.getAll().isEmpty());
        for (ClientDto client : SampleItems.clients) {
            clientDao.insert(client);

        }
        assertTrue(roomTypeDao.getAll().isEmpty());
        for (RoomTypeDto roomType : SampleItems.roomTypes) {
            roomTypeDao.insert(roomType);

        }
        assertTrue(facilityDao.getAll().isEmpty());
        for (ExtraFacilityDto extraFacility : SampleItems.facilities) {
            facilityDao.insert(extraFacility);

        }
        assertTrue(roomDao.getAll().isEmpty());
        for (RoomDto room : SampleItems.rooms) {
            roomDao.insert(room);

        }
        assertTrue(reservationDao.getAll().isEmpty());
        for (ReservationDto reservation : SampleItems.reservations) {
            reservationDao.insert(reservation);
        }
    }

    @After
    public void deleteRowsAfterTest() {
        clientDao.getAll().forEach(dto -> clientDao.delete(dto.getId()));
        assertTrue(clientDao.getAll().isEmpty());

        roomTypeDao.getAll().forEach(dto -> roomTypeDao.delete(dto.getType()));
        assertTrue(roomTypeDao.getAll().isEmpty());

        roomDao.getAll().forEach(dto -> roomDao.delete(dto.getNumber()));
        assertTrue(roomDao.getAll().isEmpty());

        facilityDao.getAll().forEach(dto -> facilityDao.delete(dto.getFacility()));
        assertTrue(facilityDao.getAll().isEmpty());

        reservationDao.getAll().forEach(dto -> reservationDao.delete(dto.getId()));
        assertTrue(reservationDao.getAll().isEmpty());
    }

    @AfterClass
    public static void deleteDbFileAfterAllTests() {
        new File(TEST_FILE).delete();
    }

    @Test
    public void getAll() {
        assertEquals(3,facilityDao.getAll());
>>>>>>> Stashed changes
    }
}
