package wantsome.project.db.service;

import wantsome.project.db.DbManager;
import wantsome.project.db.dto.RoomDto;
import wantsome.project.db.dto.RoomTypeDto;
import wantsome.project.db.dto.RoomTypes;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static wantsome.project.db.dto.PaymentMethod.CARD;
import static wantsome.project.db.dto.PaymentMethod.CASH;
import static wantsome.project.db.dto.RoomTypes.*;


/**
 * Initializes the DB as needed - creates missing tables, etc..
 * Should be called once, on app startup.
 */
public class DbInitService {


    private static final String CREATE_CLIENTS_SQL = "CREATE TABLE IF NOT EXISTS CLIENTS ( " +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "NAME TEXT NOT NULL UNIQUE, " +
            "EMAIL TEXT NOT NULL UNIQUE," +
            "ADDRESS TEXT NOT NULL" +
            ");";


    private static final String CREATE_ROOM_TYPES_SQL = "CREATE TABLE IF NOT EXISTS ROOM_TYPES ( " +
            "DESCRIPTION TEXT CHECK (DESCRIPTION IN ('" + SINGLE + "', '" + DOUBLE + "', '" + TWIN + "', '" + APARTMENT + "')) PRIMARY KEY , " +
            "PRICE DOUBLE NOT NULL," +
            "CAPACITY INTEGER NOT NULL" +
            ");";

    private static final String CREATE_ROOMS_SQL = "CREATE TABLE IF NOT EXISTS ROOMS ( " +
            "ROOM_NUMBER INTEGER PRIMARY KEY AUTOINCREMENT," +
            "ROOM_TYPE_DESCRIPTION TEXT NOT NULL REFERENCES ROOM_TYPES(DESCRIPTION), " +
            "EXTRA_INFO TEXT" +
            ");";


    private static final String CREATE_RESERVATIONS_SQL = "CREATE TABLE IF NOT EXISTS RESERVATIONS ( " +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "CLIENT_ID TEXT NOT NULL REFERENCES CLIENTS(ID), " +
            "START_DATE DATETIME NOT NULL, " +
            "END_DATE DATETIME NOT NULL, " +
            "ROOM_NUMBER INTEGER NOT NULL REFERENCES ROOMS(ROOM_NUMBER), " +
            "EXTRA_INFO TEXT, " +
            "PAYMENT_METHOD TEXT CHECK (PAYMENT_METHOD IN ('" + CARD + "', '" + CASH + "')) NOT NULL," +
            "CREATED_AT DATETIME NOT NULL " +
            ");";

    public static void createTablesAndInitialData() {
        createMissingTables();
    }

    public static void insertIntoRoomTypes() {
        RoomTypeDao roomTypeDao = new RoomTypeDao();
        List<RoomTypeDto> roomTypes = Arrays.asList(
                new RoomTypeDto(RoomTypes.DOUBLE, 55.0, 2),
                new RoomTypeDto(RoomTypes.TWIN, 50, 2),
                new RoomTypeDto(RoomTypes.SINGLE, 30, 1),
                new RoomTypeDto(RoomTypes.APARTMENT, 100, 4)
        );
        if (roomTypeDao.getAll().isEmpty()) {
            for (RoomTypeDto roomType : roomTypes) {
                roomTypeDao.insert(roomType);
            }
        }
    }

//    public static void insertIntoClients() {
//        ClientDao clientDao = new ClientDao();
//        if (clientDao.getAll().isEmpty()) {
//            clientDao.insert(new ClientDto(1, "Pal Anca", "ancapal12@gmail.com", "Pascani, jud. Iasi"));
//        }
//    }

    public static void insertIntoRooms() {
        RoomDao roomDao = new RoomDao();
        List<RoomDto> rooms = Arrays.asList(
                new RoomDto(-1, DOUBLE, "balcony access"),
                new RoomDto(-1, TWIN, "balcony access"),
                new RoomDto(-1, SINGLE, "parking lot view"),
                new RoomDto(-1, APARTMENT, "with kitchen"),
                new RoomDto(-1, DOUBLE, "matrimonial bed"),
                new RoomDto(-1, TWIN, "balcony access"),
                new RoomDto(-1, SINGLE, "near elevator"),
                new RoomDto(-1, APARTMENT, "two bathrooms"),
                new RoomDto(-1, DOUBLE, "sea view"),
                new RoomDto(-1, TWIN, "air conditioner"),
                new RoomDto(-1, SINGLE, "pets allowed"),
                new RoomDto(-1, APARTMENT, "netflix subscription"),
                new RoomDto(-1, DOUBLE, "king size bed"),
                new RoomDto(-1, TWIN, "bunk beds"),
                new RoomDto(-1, SINGLE, "parking lot view"),
                new RoomDto(-1, APARTMENT, "sea view"));

        if (roomDao.getAll().isEmpty()) {
            for (RoomDto room : rooms) {
                roomDao.insert(room);
            }
        }

    }

    private static void createMissingTables() {
        try (Connection conn = DbManager.getConnection();
             Statement st = conn.createStatement()) {

            st.execute(CREATE_RESERVATIONS_SQL);
            st.execute(CREATE_CLIENTS_SQL);
            st.execute(CREATE_ROOMS_SQL);
            st.execute(CREATE_ROOM_TYPES_SQL);

        } catch (SQLException e) {
            System.err.println("Error creating missing tables: " + e.getMessage());
        }
    }

    public static void deleteTables() {
        try (Connection conn = DbManager.getConnection();
             Statement st = conn.createStatement()) {

            st.execute("DROP TABLE RESERVATIONS;");
            st.execute("DROP TABLE CLIENTS;");
            st.execute("DROP TABLE ROOMS;");
            st.execute("DROP TABLE ROOM_TYPES;");

        } catch (SQLException e) {
            System.err.println("Error creating missing tables: " + e.getMessage());
        }
    }

}
