package wantsome.project;

import org.junit.*;
import wantsome.project.db.DbManager;
import wantsome.project.db.dto.*;
import wantsome.project.db.service.*;

import java.io.File;
import java.sql.Date;
import java.util.List;

import static org.junit.Assert.*;


public class MainTest {

    private static final String TEST_FILE = "final_project_test.db";

    private static final ClientDao clientDao = new ClientDao();
    private static final ReservationDao reservationDao = new ReservationDao();
    private static final RoomDao roomDao = new RoomDao();
    private static final RoomTypeDao roomTypeDao = new RoomTypeDao();

    @BeforeClass
    public static void initDbBeforeAnyTests() {
        DbManager.setDbFile(TEST_FILE);

    }

    @Before
    public void insertBeforeTest() {
        DbInitService.createTablesAndInitialData();

        assertTrue(roomTypeDao.getAll().isEmpty());
        for (RoomTypeDto roomType : SampleItems.roomTypes) {
            roomTypeDao.insert(roomType);
        }

        assertTrue(roomDao.getAll().isEmpty());
        for (RoomDto room : SampleItems.rooms) {
            roomDao.insert(room);
        }

        assertTrue(clientDao.getAll().isEmpty());
        for (ClientDto client : SampleItems.clients) {
            clientDao.insert(client);
        }

        assertTrue(reservationDao.getAll().isEmpty());
        for (ReservationDto reservation : SampleItems.reservations) {
            reservationDao.insert(reservation);
        }
    }

    @After
    public void deleteRowsAfterTest() {
        DbInitService.deleteTables();
    }

    @AfterClass
    public static void deleteDbFileAfterAllTests() {

        new File(TEST_FILE).delete();
    }

    @Test
    public void getAll() {
        checkOnlyTheSampleClientsArePresentInDb();
        checkOnlyTheSampleRoomsArePresentInDb();
        checkOnlyTheSampleRoomTypesArePresentInDb();
        checkOnlyTheSampleReservationsArePresentInDb();
    }

    private void checkOnlyTheSampleClientsArePresentInDb() {
        List<ClientDto> clientsFromDb = clientDao.getAll();
        assertEquals(3, clientsFromDb.size());
        assertEqualClientsExceptId(SampleItems.clients.get(0), clientsFromDb.get(0));
        assertEqualClientsExceptId(SampleItems.clients.get(2), clientsFromDb.get(2));
    }

    private void checkOnlyTheSampleRoomsArePresentInDb() {
        List<RoomDto> roomsFromDb = roomDao.getAll();
        assertEquals(4, roomsFromDb.size());
        assertEqualRoomsExceptNumber(SampleItems.rooms.get(1), roomsFromDb.get(1));
        assertEqualRoomsExceptNumber(SampleItems.rooms.get(2), roomsFromDb.get(2));
    }

    private void checkOnlyTheSampleRoomTypesArePresentInDb() {
        List<RoomTypeDto> roomTypesFromDb = roomTypeDao.getAll();
        assertEquals(4, roomTypesFromDb.size());
        assertEqualRoomTypes(SampleItems.roomTypes.get(0), roomTypesFromDb.get(0));
        assertEqualRoomTypes(SampleItems.roomTypes.get(3), roomTypesFromDb.get(3));
    }

    private void checkOnlyTheSampleReservationsArePresentInDb() {
        List<ReservationDto> reservationsFromDb = reservationDao.getAll();
        assertEquals(2, reservationsFromDb.size());
        assertEqualsReservationsExceptId(SampleItems.reservations.get(0), reservationsFromDb.get(0));
        assertEqualsReservationsExceptId(SampleItems.reservations.get(1), reservationsFromDb.get(1));
    }

    private void assertEqualClientsExceptId(ClientDto client1, ClientDto client2) {
        assertTrue("Client " + client1 + "should show the same informations as " + client2 + " except for the ID",
                client1.getName().equals(client2.getName()) &&
                        client1.getEmail().equals(client2.getEmail()) &&
                        client1.getAddress().equals(client2.getAddress()));
    }

    private void assertEqualRoomsExceptNumber(RoomDto room1, RoomDto room2) {
        assertTrue("Room " + room1 + " should be equal with room " + room2,
                room1.getRoomType().equals(room2.getRoomType()) &&
                        room1.getExtraInfo().equals(room2.getExtraInfo()));
    }

    private void assertEqualRoomTypes(RoomTypeDto roomType1, RoomTypeDto roomType2) {
        assertTrue(roomType1.getType().equals(roomType2.getType()) &&
                roomType1.getPrice() == roomType2.getPrice() &&
                roomType1.getCapacity() == roomType2.getCapacity());
    }

    private void assertEqualsReservationsExceptId(ReservationDto reservation1, ReservationDto reservation2) {
        assertTrue(reservation1.getClientId() == reservation2.getClientId() &&
                reservation1.getStartDate().equals(reservation2.getStartDate()) &&
                reservation1.getEndDate().equals(reservation2.getEndDate()) &&
                reservation1.getRoomNumber() == reservation2.getRoomNumber() &&
                reservation1.getExtraInfo().equals(reservation2.getExtraInfo()) &&
                reservation1.getPayment().equals(reservation2.getPayment()) &&
                reservation1.getCreatedAt().equals(reservation2.getCreatedAt()));
    }

    @Test
    public void get_forInvalidId() {
        assertFalse(clientDao.getById(-99).isPresent());
        assertFalse(roomDao.get(-99).isPresent());
        assertFalse(reservationDao.getById(-99).isPresent());
    }

    @Test
    public void insertNewClient() {
        assertEquals(3, clientDao.getAll().size());

        ClientDto newClient = new ClientDto(-1, "George Buhnici", "g.buhnici@gmail.com", "Bucuresti, str. Bogdan Voda");
        clientDao.insert(newClient);

        assertEqualClientsExceptId(newClient, clientDao.getAll().get(3));
    }

    @Test
    public void insertNewReservation() {
        assertEquals(2, reservationDao.getAll().size());

        ReservationDto newReservation = new ReservationDto(-1, 1, new Date(System.currentTimeMillis() + 1000), new Date(System.currentTimeMillis() + 2000), 3, "Conference", PaymentMethod.CARD);
        reservationDao.insert(newReservation);

        assertEqualsReservationsExceptId(newReservation, reservationDao.getAll().get(2));
    }
}
