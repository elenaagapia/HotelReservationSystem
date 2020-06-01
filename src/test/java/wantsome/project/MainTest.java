package wantsome.project;

import org.junit.*;
import wantsome.project.db.DbManager;
import wantsome.project.db.dto.ClientDto;
import wantsome.project.db.dto.ReservationDto;
import wantsome.project.db.dto.RoomDto;
import wantsome.project.db.dto.RoomTypeDto;
import wantsome.project.db.service.*;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class MainTest {

    private static final String TEST_FILE = "final_project_test.db";

    private static final ClientDao clientDao = new ClientDao();
    private static final ReservationDao reservationDao = new ReservationDao();
    private static final RoomDao roomDao = new RoomDao();
    private static final RoomTypeDao roomTypeDao = new RoomTypeDao();
    private static SampleItems sample = new SampleItems();

    @BeforeClass
    public static void initDbBeforeAnyTests() {//TODO:RENAME
        DbManager.setDbFile(TEST_FILE);
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

        reservationDao.getAll().forEach(dto -> reservationDao.delete(dto.getId()));
        assertTrue(reservationDao.getAll().isEmpty());
    }

    @AfterClass
    public static void deleteDbFileAfterAllTests() {

        new File(TEST_FILE).delete();
    }

    @Test
    public void getAll() {
        checkOnlyTheSampleClientsArePresentInDb();
    }

    private void checkOnlyTheSampleClientsArePresentInDb() {
        List<ClientDto> clientsFromDb = clientDao.getAll();
        assertEquals(3, clientsFromDb.size());
        assertEqualClientsExceptId(SampleItems.clients.get(0), clientsFromDb.get(0));
        assertEqualClientsExceptId(SampleItems.clients.get(2), clientsFromDb.get(2));
    }

    private void assertEqualClientsExceptId(ClientDto client1, ClientDto client2) {
        assertTrue("Client " + client1 + "should show the same informations as " + client2 + " except for the ID",
                client1.getName().equals(client2.getName()) &&
                        client1.getEmail().equals(client2.getEmail()) &&
                        client1.getAddress().equals(client2.getEmail()));
    }

    private void assertEqualRooms(RoomDto room1, RoomDto room2) {
        assertTrue("Room " + room1 + " should be equal with room " + room2,
                room1.getNumber() == room2.getNumber() &&
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
                reservation1.getExtraFacilities().equals(reservation2.getExtraFacilities()) &&
                reservation1.getPayment().equals(reservation2.getPayment()) &&
                reservation1.getCreatedAt().equals(reservation2.getCreatedAt()));
    }
}
