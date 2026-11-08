package br.com.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DbHelper {

    public static List<Map<String, Object>> query(String sql, Object... params) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();
        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setParams(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int colCount = meta.getColumnCount();
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    for (int i = 1; i <= colCount; i++) {
                        String colName = meta.getColumnLabel(i);
                        Object val = rs.getObject(i);
                        if (val instanceof Timestamp) {
                            row.put(colName, val.toString().replace(".0", ""));
                        } else if (val instanceof java.sql.Date) {
                            row.put(colName, val.toString());
                        } else {
                            row.put(colName, val);
                        }
                    }
                    results.add(row);
                }
            }
        }
        return results;
    }

    public static Object scalar(String sql, Object... params) throws SQLException {
        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setParams(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getObject(1);
                }
            }
        }
        return null;
    }

    public static int executeUpdate(String sql, Object... params) throws SQLException {
        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setParams(ps, params);
            return ps.executeUpdate();
        }
    }

    public static int executeInsert(String sql, Object... params) throws SQLException {
        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParams(ps, params);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    private static void setParams(PreparedStatement ps, Object... params) throws SQLException {
        if (params == null) return;
        for (int i = 0; i < params.length; i++) {
            Object p = params[i];
            if (p == null) {
                ps.setNull(i + 1, java.sql.Types.NULL);
            } else if (p instanceof BigDecimal) {
                ps.setBigDecimal(i + 1, (BigDecimal) p);
            } else if (p instanceof Number) {
                ps.setObject(i + 1, p);
            } else {
                ps.setString(i + 1, p.toString());
            }
        }
    }
}
