package wantsome.project.ui.web;

import spark.Request;
import spark.Response;
import wantsome.project.db.dto.ClientDto;
import wantsome.project.db.service.ClientDao;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static wantsome.project.ui.web.SparkUtil.render;

public class AddEditClientsPageController {

    private static final ClientDao clientDao = new ClientDao();

    public static String showAddForm(Request req, Response res) {
        return renderAddUpdateForm("", "", "", "", "");
    }

    public static String showUpdateForm(Request req, Response res) {
        String id = req.params("id");
        try {
            Optional<ClientDto> optClient = clientDao.getById(Integer.parseInt(id));
            if (optClient.isPresent()) {
                ClientDto client = optClient.get();
                return renderAddUpdateForm(String.valueOf(client.getId()), client.getName(), client.getEmail(), client.getAddress(), "");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading client with id: " + id + ": " + e.getMessage());
        }
        return "Error: client with id:  " + id + " not found!";
    }

    private static String renderAddUpdateForm(String id, String name, String email, String address, String errorMessage) {
        Map<String, Object> model = new HashMap<>();
        model.put("prevId", id);
        model.put("prevName", name);
        model.put("prevEmail", email);
        model.put("prevAddress", address);
        model.put("errorMsg", errorMessage);
        model.put("isUpdate", id != null && !id.isEmpty());
        return render(model, "add_edit_client.vm");
    }


    public static Object handleAddUpdateRequest(Request req, Response res) {

        String id = req.queryParams("id");
        String name = req.queryParams("name");
        String email = req.queryParams("email");
        String address = req.queryParams("address");

        try {
            ClientDto client = validateAndInsertClient(id, name, email, address);

            if (id != null && !id.isEmpty()) { //update case
                clientDao.update(client);
            } else {
                clientDao.insert(client);
            }

            res.redirect("/clients");
            return res;

        } catch (Exception e) {
            return renderAddUpdateForm(id, name, email, address, e.getMessage());
        }
    }

    private static ClientDto validateAndInsertClient(String id, String name, String email, String address) {
        long idValue = id != null && !id.isEmpty() ? Long.parseLong(id) : -1;
        if (name == null || name.isEmpty()) {
            throw new RuntimeException("Name is required!");
        }
        if (email == null || email.isEmpty()) {
            throw new RuntimeException("Email is required!");
        }
        if (address == null || address.isEmpty()) {
            throw new RuntimeException("Address is required!");
        }
        return new ClientDto(idValue, name, email, address);
    }
}


