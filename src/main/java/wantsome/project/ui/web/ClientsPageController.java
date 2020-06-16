package wantsome.project.ui.web;

import spark.Request;
import spark.Response;
import wantsome.project.db.dto.ClientDto;
import wantsome.project.db.service.ClientDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static wantsome.project.ui.web.SparkUtil.render;

public class ClientsPageController {

    private static final ClientDao clientsDao = new ClientDao();

    public static String showClientsPage(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        List<ClientDto> allClients = clientsDao.getAll();
        model.put("clients", allClients);
        return render(model, "clients.vm");
    }

    public static Object handleDeleteRequest(Request req, Response res) {
        String id = req.params("id");
        try {
            clientsDao.delete(Long.parseLong(id));
        } catch (Exception e) {
            throw new RuntimeException("Error deleting client with id '" + id + "':" + e.getMessage());
        }
        res.redirect("/clients");
        return res;
    }


}
