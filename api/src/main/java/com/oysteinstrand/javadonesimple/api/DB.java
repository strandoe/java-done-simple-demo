package com.oysteinstrand.javadonesimple.api;

import com.oysteinstrand.javadonesimple.common.SQL;
import org.hsqldb.jdbc.JDBCDataSource;

public class DB {
    public static final JDBCDataSource ds;

    static {
        ds = new JDBCDataSource();
        ds.setUrl("jdbc:hsqldb:file:javadonesimple");
        ds.setUser("sa");
    }

    public static void createUsersTable() {
        SQL.update(ds, "CREATE TABLE users(id BIGINT, name VARCHAR(255));");
    }

    public static void createUserIdSeq() {
        SQL.update(ds, "CREATE SEQUENCE user_id_seq AS BIGINT START WITH 1 INCREMENT BY 1;");
    }

    public static void createSomeUsers() {
        SQL.nesteSekvensverdi(ds, "user_id_seq");
        Long id1 = SQL.nesteSekvensverdi(ds, "user_id_seq");
        Long id2 = SQL.nesteSekvensverdi(ds, "user_id_seq");
        SQL.update(ds, "insert into users values(?, ?)", id1, "Øystein Strand");
        SQL.update(ds, "insert into users values(?, ?)", id2, "Christina Sivertsen");
    }

    public static void main(String[] args) {
        createUsersTable();
        createUserIdSeq();
        createSomeUsers();
    }
}
