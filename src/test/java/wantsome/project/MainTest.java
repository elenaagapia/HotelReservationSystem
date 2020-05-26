package wantsome.project;

import org.junit.*;
import wantsome.project.db.DbManager;
import wantsome.project.db.dto.ClientDto;
import wantsome.project.db.dto.ReservationDto;
import wantsome.project.db.dto.RoomDto;
import wantsome.project.db.dto.RoomTypeDto;
import wantsome.project.db.service.*;

import java.io.File;

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
    }
}
