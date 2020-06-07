package wantsome.project.ui.web;

import spark.Request;
import spark.Response;
import wantsome.project.db.dto.RoomDto;
import wantsome.project.db.dto.RoomTypes;
import wantsome.project.db.service.RoomDao;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static wantsome.project.ui.web.SparkUtil.render;

public class AddEditRoomsPageController {

    private static final RoomDao roomDao = new RoomDao();

    public static String showAddForm(Request req, Response res) {
        return renderAddUpdateForm("", "", "", "");
    }

    public static String showUpdateForm(Request req, Response res) {
        String number = req.params("number");
        try {
            Optional<RoomDto> optRoom = roomDao.get(Integer.parseInt(number));
            if (optRoom.isPresent()) {
                RoomDto room = optRoom.get();
                return renderAddUpdateForm(String.valueOf(room.getNumber()), room.getRoomType().name(), room.getExtraInfo(), "");
            }
        } catch (Exception e) {
            System.err.println("Error loading room with number: " + number + ": " + e.getMessage());
        }
        return "Error: room with number:  " + number + " not found!";
    }

    private static String renderAddUpdateForm(String number, String roomType, String extraInfo, String errorMessage) {
        Map<String, Object> model = new HashMap<>();
        model.put("prevNumber", number);
        model.put("prevRoomType", roomType);
        model.put("prevExtraInfo", extraInfo);
        model.put("errorMsg", errorMessage);
        model.put("isUpdate", number != null && !number.isEmpty());
        return render(model, "add_edit_room.vm");
    }


    public static Object handleAddUpdateRequest(Request req, Response res) {

        String number = req.queryParams("number");
        String roomType = req.queryParams("roomType");
        String extraInfo = req.queryParams("extraInfo");

        try {
            RoomDto room = validateAndBuildRoom(number, roomType, extraInfo);

            if (number != null && !number.isEmpty()) { //update case
                roomDao.update(room);
            } else {
                roomDao.insert(room);
            }

            res.redirect("/rooms");
            return res;

        } catch (Exception e) {
            return renderAddUpdateForm(number, roomType, extraInfo, e.getMessage());
        }
    }

    private static RoomDto validateAndBuildRoom(String number, String roomType, String extraInfo) {
        long numberValue = number != null && !number.isEmpty() ? Long.parseLong(number) : -1;

        if (roomType == null || roomType.isEmpty()) {
            throw new RuntimeException("Room type is required!");
        }
        RoomTypes roomTypeValue;
        try {
            roomTypeValue = RoomTypes.valueOf(roomType);
        } catch (Exception e) {
            throw new RuntimeException("Invalid room type: '" + roomType +
                    "', must be one of: " + Arrays.toString(RoomTypes.values()));
        }

        return new RoomDto(numberValue, roomTypeValue, extraInfo);
    }
}

