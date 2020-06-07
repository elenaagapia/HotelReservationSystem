package wantsome.project.ui.web;

import spark.Request;
import spark.Response;
import wantsome.project.db.dto.RoomDto;
import wantsome.project.db.service.RoomDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static wantsome.project.ui.web.SparkUtil.render;

public class RoomsPageController {

    private static final RoomDao roomsDao = new RoomDao();

    public static String showRoomsPage(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        List<RoomDto> allRooms = roomsDao.getAll();
        model.put("rooms", allRooms);
        return render(model, "rooms.vm");
    }

    public static Object handleDeleteRequest(Request req, Response res) {
        String number = req.params("number");
        try {
            roomsDao.delete(Long.parseLong(number));
        } catch (Exception e) {
            System.out.println("Error deleting room with number '" + number + "':" + e.getMessage());
        }
        res.redirect("/rooms");
        return res;
    }
}
