package com.oysteinstrand.javadonesimple.front;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import spark.ModelAndView;
import spark.TemplateViewRoute;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class HandlebarsTemplateViewRoute extends TemplateViewRoute {

    private Handlebars handlebars;
    private static Map<String, Template> templates = new HashMap<String, Template>();

    protected HandlebarsTemplateViewRoute(String path) {
        super(path, "text/x-handlebars-template");
        handlebars = new Handlebars();
    }

    @Override
    public String render(ModelAndView modelAndView) {
        String view = modelAndView.getViewName();
        Template template = templates.get(view);
        try {
            template = template == null ? handlebars.compile(view) : template;
            return template.apply(modelAndView.getModel());
        } catch (IOException e) {
            throw new RuntimeException("Could not load/apply template: " + view);
        }
    }
}
