package wantsome.project.ui.web;

import spark.Request;
import spark.Response;
import wantsome.project.db.dto.RoomTypeDto;
import wantsome.project.db.service.RoomTypeDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static wantsome.project.ui.web.SparkUtil.render;

public class RoomTypesPageController {
    private static final RoomTypeDao roomTypeDao = new RoomTypeDao();

    public static String showRoomTypesPage(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        List<RoomTypeDto> allRoomTypes = roomTypeDao.getAll();
        model.put("roomTypes", allRoomTypes);
        return render(model, "room_types.vm");
    }
}
