package com.revworkforce.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Statement;

public class DatabaseSetup {

    public static void initializeDatabase() {
        String schemaPath = "schema.sql";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                BufferedReader br = new BufferedReader(new FileReader(schemaPath))) {

            System.out.println("Checking database schema...");

            // Allow multiple queries separated by ;
            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().startsWith("--") || line.trim().isEmpty())
                    continue;
                sql.append(line);
                if (line.trim().endsWith(";")) {
                    try {
                        // Very basic execution - in a real app, use a migration tool like Flyway
                        stmt.execute(sql.toString());
                    } catch (Exception e) {
                        // Ignore errors if table already exists, or print friendly warning
                        // System.err.println("Schema warning: " + e.getMessage());
                    }
                    sql.setLength(0);
                }
            }
            System.out.println("Database schema initialized.");

        } catch (Exception e) {
            System.err.println("Database setup warning: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        initializeDatabase();
    }
}
