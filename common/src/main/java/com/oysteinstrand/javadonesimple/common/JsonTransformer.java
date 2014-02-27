package com.oysteinstrand.javadonesimple.common;

import com.google.gson.Gson;
import spark.ResponseTransformerRoute;

public abstract class JsonTransformer extends ResponseTransformerRoute {

    private static final String jsonContentType = "application/json";
    private Gson gson = new Gson();

    protected JsonTransformer(String path) {
        super(path, jsonContentType);
    }

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }
}
