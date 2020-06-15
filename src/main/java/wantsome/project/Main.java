package wantsome.project;

import wantsome.project.db.service.DbInitService;
import wantsome.project.ui.web.*;

import static spark.Spark.*;

/**
 * Main class of the application
 */
public class Main {

    public static void main(String[] args) {
        setup();
        configureRoutesAndStart();
    }

    private static void setup() {
        //create and configure all needed resources (db tables, etc)
        DbInitService.createTablesAndInitialData();
        DbInitService.insertIntoRoomTypes();
        DbInitService.insertIntoRooms();

    }

    private static void configureRoutesAndStart() {
        staticFileLocation("public");

        //---RESERVATIONS---//
        get("/main", ReservationsPageController::showReservationsPage);

        get("/add", AddEditReservationPageController::showAddForm);
        post("/add", AddEditReservationPageController::handleAddUpdateRequest);

        get("/update/:id", AddEditReservationPageController::showUpdateForm);
        post("/update/:id", AddEditReservationPageController::handleAddUpdateRequest);

        get("/delete/:id", ReservationsPageController::handleDeleteRequest);
        get("/invoice/:id", InvoicePageController::showInvoiceForReservation);

        //---CLIENTS---//
        get("/clients", ClientsPageController::showClientsPage);

        get("/add_client", AddEditClientsPageController::showAddForm);
        post("/add_client", AddEditClientsPageController::handleAddUpdateRequest);

        get("update_client/:id", AddEditClientsPageController::showUpdateForm);
        post("update_client/:id", AddEditClientsPageController::handleAddUpdateRequest);

        get("/delete_client/:id", ClientsPageController::handleDeleteRequest);

        //---ROOMS---//

        get("/rooms", RoomsPageController::showRoomsPage);

        get("/add_room", AddEditRoomsPageController::showAddForm);
        post("/add_room", AddEditRoomsPageController::handleAddUpdateRequest);

        get("update_room/:number", AddEditRoomsPageController::showUpdateForm);
        post("update_room/:number", AddEditRoomsPageController::handleAddUpdateRequest);

        get("/delete_room/:number", RoomsPageController::handleDeleteRequest);

        //---ROOM TYPES---//
        get("/roomTypes", RoomTypesPageController::showRoomTypesPage);

        awaitInitialization();
        System.out.println("\nServer started, url: http://localhost:4567/main\n");

        exception(Exception.class, ErrorPageController::handleException);
    }
}
