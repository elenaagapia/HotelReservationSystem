package wantsome.project.ui.web;

import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

import static wantsome.project.ui.web.SparkUtil.render;

public class ErrorPageController {

    public static void handleException(Exception exception, Request request, Response response) {
        Map<String, Object> model = new HashMap<>();
        model.put("errorMsg", exception.getMessage());
        response.body(render(model, "error.vm"));
    }
}
