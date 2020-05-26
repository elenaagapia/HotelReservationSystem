package wantsome.project.db.service;

import wantsome.project.db.DbManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static wantsome.project.db.dto.ExtraServices.*;
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
            "NUMBER INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "ROOM_TYPE_DESCRIPTION TEXT REFERENCES ROOM_TYPES(DESCRIPTION) " +
            "EXTRA_INFO TEXT" +
            ");";

    private static final String CREATE_EXTRA_FACILITIES_SQL = "CREATE TABLE IF NOT EXISTS EXTRA_FACILITIES ( " +
            "FACILITY TEXT CHECK (FACILITY IN ('" + PARKING_SPACE + "', '" + BREAKFAST + "', '" + SPA_MEMBERSHIP + "', '" + TRANSIT_TRANSPORTATION + "', '" + DAY_CARE + "'))) PRIMARY KEY, " +
            "PRICE DOUBLE NOT NULL" +
            ");";

    private static final String CREATE_RESERVATIONS_SQL = "CREATE TABLE IF NOT EXISTS RESERVATIONS ( " +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "CLIENT_ID INTEGER NOT NULL REFERENCES CLIENTS(ID), " +
            "START_DATE DATETIME NOT NULL, " +
            "END_DATE DATETIME NOT NULL, " +
            "ROOM_NUMBER INTEGER NOT NULL, " +
            "EXTRA_FACILITY TEXT REFERENCES EXTRA_FACILITIES(FACILITY)," +
            "EXTRA_INFO TEXT, " +
            "PAYMENT_METHOD TEXT CHECK(PAYMENT_METHOD IN ('" + CARD + "', '" + CASH + "'))" +
            "CREATED_AT DATETIME NOT NULL " +
            ");";

    public static void createTablesAndInitialData() {
        createMissingTables();
    }

    private static void createMissingTables() {
        try (Connection conn = DbManager.getConnection();
             Statement st = conn.createStatement()) {

            st.execute(CREATE_RESERVATIONS_SQL);
            st.execute(CREATE_CLIENTS_SQL);
            st.execute(CREATE_ROOMS_SQL);
            st.execute(CREATE_ROOM_TYPES_SQL);
            st.execute(CREATE_EXTRA_FACILITIES_SQL);

        } catch (SQLException e) {
            System.err.println("Error creating missing tables: " + e.getMessage());
        }
    }

}
