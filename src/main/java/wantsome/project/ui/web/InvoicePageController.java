package wantsome.project.ui.web;

import spark.Request;
import spark.Response;
import wantsome.project.db.dto.ReservationDto;
import wantsome.project.db.dto.RoomTypes;
import wantsome.project.db.service.ClientDao;
import wantsome.project.db.service.ReservationDao;
import wantsome.project.db.service.RoomDao;
import wantsome.project.db.service.RoomTypeDao;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static wantsome.project.ui.web.SparkUtil.render;

public class InvoicePageController {
    private static final ReservationDao reservationDao = new ReservationDao();
    private static final ClientDao clientDao = new ClientDao();
    private static final RoomDao roomDao = new RoomDao();
    private static final RoomTypeDao roomTypeDao = new RoomTypeDao();


    public static String showInvoiceForReservation(Request req, Response res) {
        LocalDate todayDate = LocalDate.now();
        Map<String, Object> model = new HashMap<>();
        String reservationId = req.params("id");

        try {
            Optional<ReservationDto> optReservation = reservationDao.getById(Integer.parseInt(reservationId));
            if (optReservation.isPresent()) {
                ReservationDto reservation = optReservation.get();
                String clientName = reservation.getClientName();
                RoomTypes roomType = roomDao.get(reservation.getRoomNumber()).get().getRoomType();
                double price = roomTypeDao.get(roomType).get().getPrice();

                LocalDate startDate = reservation.getStartDate().toLocalDate();
                LocalDate endDate = reservation.getEndDate().toLocalDate();
                Period diff = Period.between(startDate, endDate);

                int periodOfTime = diff.getDays();

                double totalPrice = price * periodOfTime;


                model.put("name", clientName);
                model.put("address", clientDao.getByName(clientName).get().getAddress());
                model.put("email", clientDao.getByName(clientName).get().getEmail());
                model.put("todayDate", todayDate);
                model.put("roomType", roomType);
                model.put("roomNumber", reservation.getRoomNumber());
                model.put("price", price);
                model.put("periodOfTime", periodOfTime);
                model.put("totalPrice", totalPrice);

                return render(model, "invoice.vm");
            }
        } catch (Exception e) {
            System.err.println("Error loading invoice " + e.getMessage());
        }
        return "Error";
    }
}
