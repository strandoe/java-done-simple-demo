package com.oysteinstrand.javadonesimple.common;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SQL {

    private static final Logger LOG = LoggerFactory.getLogger(SQL.class);

    public static <T> T query(DataSource ds, final RowMapper<T> rowMapper, final String preparedStatement, final Object... args) {
        List<T> results = queryForList(ds, rowMapper, preparedStatement, args);
        return results.isEmpty() ? null : results.get(0);
    }

    public static <T> List<T> queryForList(DataSource ds, final RowMapper<T> rowMapper, final String preparedStatement, final Object... args) {
        List<T> results = doWithConnection(ds, new Callback<List<T>>() {
            public List<T> kjor(Connection connection) throws SQLException {
                try (PreparedStatement statement = connection.prepareStatement(preparedStatement)) {
                    LOG.info("[SQL] " + preparedStatement + " " + Arrays.asList(args));
                    prepare(statement, args);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        return getResults(resultSet, rowMapper);
                    }
                }
            }
        });
        return results;
    }

    public static Integer update(DataSource ds, final String preparedStatement, final Object... args) {
        return doWithConnection(ds, new Callback<Integer>() {
            public Integer kjor(Connection connection) throws SQLException {
                try (PreparedStatement statement = connection.prepareStatement(preparedStatement)) {
                    LOG.info("[SQL] " + preparedStatement + " " + Arrays.asList(args));
                    prepare(statement, args);
                    return statement.executeUpdate();
                }
            }
        });
    }

    public static Long nesteSekvensverdi(DataSource ds, final String sekvensnavn) {
        return query(ds, new RowMapper.LongMapper(), "call NEXT VALUE FOR " + sekvensnavn + ";");
//        return query(ds, new RowMapper.LongMapper(), "select " + sekvensnavn + ".nextval from dual");
    }

    private static <T> List<T> getResults(ResultSet rs, final RowMapper<T> rowMapper) throws SQLException {
        List<T> results = new ArrayList<>();
        while (rs.next()) {
            results.add(rowMapper.map(rs));
        }
        return results;
    }

    private static <T> T doWithConnection(DataSource ds, Callback<T> callback) {
        try (Connection connection = ds.getConnection()) {
            return callback.kjor(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private interface Callback<T> {
        T kjor(Connection connection) throws SQLException;
    }

    public static Integer getInteger(ResultSet rs, String kolonne) throws SQLException {
        int tall = rs.getInt(kolonne);
        return rs.wasNull() ? null : tall;
    }

    public static Boolean getBoolean(ResultSet rs, String kolonne) throws SQLException {
        int tall = rs.getInt(kolonne);
        if (rs.wasNull()) {
            return null;
        }
        if (tall > 1) {
            throw new IllegalStateException("Boolean kolonne '" + kolonne + "' inneholder verdien " + tall);
        }
        return tall == 1;
    }

    public static DateTime getDateTime(ResultSet rs, String kolonne) throws SQLException {
        Timestamp sqlTimestamp = rs.getTimestamp(kolonne);
        return sqlTimestamp == null ? null : new DateTime(sqlTimestamp.getTime());
    }

    private static void prepare(PreparedStatement st, Object... args) throws SQLException {
        for (int index = 0; index < args.length; index++) {
            if (args[index] == null) {
                st.setObject(index + 1, null);
                continue;
            }
            if (args[index] instanceof String) {
                st.setString(index + 1, (String) args[index]);
            } else if (args[index] instanceof Integer) {
                st.setInt(index + 1, (Integer) args[index]);
            } else if (args[index] instanceof DateTime) {
                long time = ((DateTime) args[index]).getMillis();
                st.setTimestamp(index + 1, new Timestamp(time));
            } else if (args[index] instanceof Enum) {
                st.setString(index + 1, ((Enum<?>) args[index]).name());
            } else if (args[index] instanceof Long) {
                st.setLong(index + 1, ((Long) args[index]));
            } else if (args[index] instanceof Boolean) {
                st.setInt(index + 1, ((Boolean) args[index]) ? 1 : 0);
            } else if (args[index] instanceof InputStream) {
                st.setBinaryStream(index + 1, ((InputStream) args[index]));
            } else {
                throw new IllegalArgumentException("ukjent type for verdi: " + args[index]);
            }
        }
    }

}
