package org.example.util;

import java.sql.SQLException;

public final class SqlErrors {

    private SqlErrors() {
    }

    public static boolean isDuplicateKey(SQLException e) {

        return "23000".equals(e.getSQLState());
    }
    public static boolean isIntegrityViolation(SQLException e) {
        return "23000".equals(e.getSQLState());
    }
}