package org.vtinstitute.tools;

import org.vtinstitute.connection.Database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class DBReader {

    public static void main(String[] args) {
        Database db = new Database();
        try (Connection conn = db.openConnection()) {
            if (conn == null) {
                System.err.println("No se pudo establecer conexión con la base de datos.");
                return;
            }

            DatabaseMetaData meta = conn.getMetaData();
            try (ResultSet tables = meta.getTables(null, "public", "%", new String[]{"TABLE"})) {
                System.out.println("Tablas en el esquema public:");
                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    System.out.println("- " + tableName);

                    // Mostrar hasta 5 filas de la tabla (si existen)
                    String query = String.format("SELECT * FROM %s LIMIT 5", tableName);
                    try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(query)) {
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int cols = rsmd.getColumnCount();
                        int rowCount = 0;
                        while (rs.next()) {
                            rowCount++;
                            StringBuilder sb = new StringBuilder();
                            sb.append("  [row " + rowCount + "] ");
                            for (int i = 1; i <= cols; i++) {
                                String colName = rsmd.getColumnName(i);
                                Object val = rs.getObject(i);
                                sb.append(colName).append("=").append(val).append(i==cols?"":" | ");
                            }
                            System.out.println(sb.toString());
                        }
                        if (rowCount == 0) {
                            System.out.println("  (sin filas)");
                        }
                    } catch (Exception e) {
                        System.err.println("  No se pudo leer filas de " + tableName + ": " + e.getMessage());
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Error durante lectura de la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
