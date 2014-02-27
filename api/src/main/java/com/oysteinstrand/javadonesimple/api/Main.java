package com.oysteinstrand.javadonesimple.api;

import com.oysteinstrand.javadonesimple.common.JsonTransformer;
import com.oysteinstrand.javadonesimple.common.Record;
import com.oysteinstrand.javadonesimple.common.RowMapper;
import com.oysteinstrand.javadonesimple.common.SQL;
import org.hsqldb.jdbc.JDBCDataSource;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.setPort;

public class Main {
    static {
        setPort(9090);
//        DB.main(new String[]{});
    }

    public static void main(String[] args) {
        JDBCDataSource ds = DB.ds;
        final List<Record<User>> users = SQL.queryForList(ds, new RowMapper<Record<User>>() {
            @Override
            public Record<User> map(ResultSet rs) throws SQLException {
                return new Record<User>()
                        .with(User.id, rs.getLong("id"))
                        .with(User.name, rs.getString("name"));
            }
        }, "select * from users");

        get(new JsonTransformer("/users/*") {
            @Override
            public Object handle(Request request, Response response) {
                response.header("Content-Type", "application/json");
                return users;
            }
        });
    }
}
