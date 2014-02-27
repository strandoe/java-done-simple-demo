package com.oysteinstrand.javadonesimple.front;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.InputSupplier;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class UserService {
    public List<Map<String, Object>> fetchAll() {
        final HttpResponse httpResponse;
        final String content;
        final HttpClient client = HttpClients.createDefault();
        final Gson gson = new Gson();
        final HttpGet request = new HttpGet("http://localhost:9090/users/");
        request.setHeader("Accept", "application/json");
        try {
            httpResponse = client.execute(request);
            InputSupplier<InputStreamReader> readerSupplier = CharStreams.newReaderSupplier
                    (new InputSupplier<InputStream>() {
                        @Override
                        public InputStream getInput() {
                            try {
                                return httpResponse.getEntity().getContent();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, Charsets.UTF_8);
            content = CharStreams.toString(readerSupplier);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Type type = new TypeToken<List<Map<String, Object>>>(){}.getType();
        List<Map<String, Object>> users;
        try {
            users = gson.fromJson(content, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return users;
    }
}
