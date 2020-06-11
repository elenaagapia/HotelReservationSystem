package wantsome.project.ui.web;

import spark.Request;
import spark.Response;
import wantsome.project.db.dto.ReservationDto;
import wantsome.project.db.service.ReservationDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static wantsome.project.ui.web.SparkUtil.render;

public class ReservationsPageController {


    private static final ReservationDao reservationDao = new ReservationDao();

    public static String showReservationsPage(Request req, Response res) {
        // hide past reservations
        boolean hidePastReservations = getHideOldReservationsFromParamOrSes(req);

        List<ReservationDto> allReservations = reservationDao.getAllOrderedByStartDate();
        List<ReservationDto> activeReservations = reservationDao.getActiveReservationsOrderedByDate();
        long activeCount = activeReservations.stream().count();
        long oldReservationsCount = allReservations.size() - activeCount;
        Map<String, Object> model = new HashMap<>();

        if (hidePastReservations) {
            model.put("reservations", activeReservations);
        } else {
            model.put("reservations", allReservations);
        }

        model.put("activeCount", activeCount);
        model.put("oldReservationsCount", oldReservationsCount);
        model.put("hidePastReservations", hidePastReservations);


        return render(model, "reservations.vm");
    }

    private static boolean getHideOldReservationsFromParamOrSes(Request req) {
        String param = req.queryParams("hidePastReservations"); //read it from current request (if sent)
        if (param != null) {
            req.session().attribute("hidePastReservations", param); //save it to session for later
        } else {
            param = req.session().attribute("hidePastReservations"); //try to read it from session
        }
        return "true".equals(param); //reverse comparison to avoid NPE if param is still null
    }

    public static Object handleDeleteRequest(Request req, Response res) {
        String id = req.params("id");
        try {
            reservationDao.delete(Long.parseLong(id));
        } catch (Exception e) {
            System.out.println("Error deleting reservation with id '" + id + "': " + e.getMessage());
        }
        res.redirect("/main");
        return res;
    }


}
