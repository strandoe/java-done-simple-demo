package com.oysteinstrand.javadonesimple.front;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class Main {
    public static final String rootPath = "/front";
    static {
        staticFileLocation("/public");
        setPort(9095);
    }

    public static void main(String[] args) {
        final UserService service = new UserService();
        get(new HandlebarsTemplateViewRoute(rootPath) {
            @Override
            public Object handle(Request request, Response response) {
                List<Map<String, Object>> user = service.fetchAll();
                return new ModelAndView(user, "templates/index");
            }
        });
    }
}
