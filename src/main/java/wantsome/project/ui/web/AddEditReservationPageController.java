package wantsome.project.ui.web;

import spark.Request;
import spark.Response;
import wantsome.project.db.dto.ClientDto;
import wantsome.project.db.dto.PaymentMethod;
import wantsome.project.db.dto.ReservationDto;
import wantsome.project.db.service.ClientDao;
import wantsome.project.db.service.ReservationDao;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static wantsome.project.ui.web.SparkUtil.render;

public class AddEditReservationPageController {

    private static final ReservationDao reservationDao = new ReservationDao();
    private static final ClientDao clientDao = new ClientDao();

    public static String showAddForm(Request req, Response res) {
        return renderAddUpdateForm("", "", "", "", "", "", "", "", "");
    }

    public static String showUpdateForm(Request req, Response res) {
        String id = req.params("id");
        try {
            Optional<ReservationDto> optReservation = reservationDao.getById(Integer.parseInt(id));
            if (optReservation.isPresent()) {
                ReservationDto reservation = optReservation.get();
                return renderAddUpdateForm(
                        String.valueOf(reservation.getId()),
                        String.valueOf(reservation.getClientId()),
                        reservation.getStartDate().toString(),
                        reservation.getEndDate().toString(),
                        String.valueOf(reservation.getRoomNumber()),
                        reservation.getExtraInfo() != null ? reservation.getExtraInfo() : "",
                        reservation.getPayment().name(),
                        reservation.getCreatedAt().toString()
                        , "");
            }
        } catch (Exception e) {
            System.err.println("Error loading reservation with id '" + id + "': " + e.getMessage());
        }
        return "Error: reservation " + id + " not found!";
    }

    private static String renderAddUpdateForm(String id, String clientId, String startDate,
                                              String endDate, String roomNumber, String extraInfo,
                                              String paymentMethod, String createdAt, String errorMessage) {
        Map<String, Object> model = new HashMap<>();
        List<ClientDto> clients = clientDao.getAll();
        LocalDate todayDate = LocalDate.now();

        model.put("prevId", id);
        model.put("prevClientId", clientId);
        model.put("prevStartDate", startDate);
        model.put("prevEndDate", endDate);
        model.put("prevRoomNumber", roomNumber);
        model.put("prevExtraInfo", extraInfo);
        model.put("prevPaymentMethod", paymentMethod);
        model.put("prevCreatedAt", createdAt);
        model.put("errorMsg", errorMessage);
        model.put("isUpdate", id != null && !id.isEmpty());
        model.put("clients", clients);
        model.put("todayDate", todayDate);

        return render(model, "addform.vm");
    }

    public static Object handleAddUpdateRequest(Request req, Response res) {
        //read form values (posted as params)
        String id = req.queryParams("generatedId");
        String clientId = req.queryParams("clientId");
        String startDate = req.queryParams("startDate");
        String endDate = req.queryParams("endDate");
        String roomNumber = req.queryParams("roomNumber");
        String extraInfo = req.queryParams("extraInfo");
        String paymentMethod = req.queryParams("paymentMethod");
        String createdAt = req.queryParams("createdAt");


        boolean isUpdateCase = id != null && !id.isEmpty();

        try {
            ReservationDto reservation = validateAndBuildReservation(id, clientId, startDate, endDate, roomNumber, extraInfo, paymentMethod);

            if (isUpdateCase) {
                reservationDao.update(reservation);
            } else {
                reservationDao.insert(reservation);
            }

            res.redirect("/main");
            return res;

        } catch (Exception e) {
            return renderAddUpdateForm(id, clientId, startDate, endDate, roomNumber, extraInfo, paymentMethod, createdAt, e.getMessage());
        }
    }

    private static ReservationDto validateAndBuildReservation(String id, String clientId, String startDate,
                                                              String endDate, String roomNumber, String extraInfo, String paymentMethod) {

        long idValue = id != null && !id.isEmpty() ? Long.parseLong(id) : -1;

        long clientIdValue = Long.parseLong(clientId);

        if (startDate == null || startDate.isEmpty()) {
            throw new RuntimeException("Start date is required!");
        }
        Date startDateValue;
        try {
            startDateValue = Date.valueOf(startDate);
        } catch (Exception e) {
            throw new RuntimeException("Invalid due date value: '" + startDate +
                    "', must be a date in format: yyyy-[m]m-[d]d");
        }

        if (endDate == null || endDate.isEmpty() || endDate.compareTo(startDate) <= 0) {
            throw new RuntimeException("End date is required and should be after the start date!");
        }
        Date endDateValue;
        try {
            endDateValue = Date.valueOf(endDate);
        } catch (Exception e) {
            throw new RuntimeException("Invalid due date value: '" + endDate +
                    "', must be a date in format: yyyy-[m]m-[d]d");
        }

        if (roomNumber == null || roomNumber.isEmpty()) {
            throw new RuntimeException("Room number is required!");
        }
        long roomNumberValue;
        try {
            roomNumberValue = Long.parseLong(roomNumber);
        } catch (Exception e) {
            throw new RuntimeException("Invalid room number : '" + roomNumber);
        }

        if (paymentMethod == null || paymentMethod.isEmpty()) {
            throw new RuntimeException("Payment method is required!");
        }
        PaymentMethod paymentValue;
        try {
            paymentValue = PaymentMethod.valueOf(paymentMethod);
        } catch (Exception e) {
            throw new RuntimeException("Invalid payment method: '" + paymentMethod +
                    "', must be one of: " + Arrays.toString(PaymentMethod.values()));
        }


        return new ReservationDto(idValue, clientIdValue, startDateValue, endDateValue, roomNumberValue, extraInfo, paymentValue);
    }

}
