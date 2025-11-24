package org.vtinstitute.controller;

import org.vtinstitute.connection.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoresController {
    private Database db = new Database();

    // Function that give us every passed subjects.
    public List<Map<String, Object>> getPassedSubjects(Integer idEnrollment) {
        String sql = "SELECT * FROM subjects_passed_IRH_2526(?)"; // Corregido el FROM
        List<Map<String, Object>> lista = new ArrayList<>();

        try (Connection conn = db.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEnrollment);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> result = new HashMap<>();
                result.put("subject", rs.getString("subject"));
                result.put("score", rs.getInt("score"));
                result.put("year", rs.getInt("year"));

                lista.add(result);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    

    // Function that give us every not passed subjects.
    public List<Map<String, Object>> getNotPassedSubjects(Integer idEnrollment) {
        String sql = "SELECT * FROM subjects_passed_IRH_2526(?)"; // Corregido el FROM
        List<Map<String, Object>> lista = new ArrayList<>();

        try (Connection conn = db.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEnrollment);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> result = new HashMap<>();
                result.put("subject", rs.getString("subject"));
                result.put("score", rs.getInt("score"));
                result.put("year", rs.getInt("year"));

                lista.add(result);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Function that adds new Scores to the database.
    public void addNewScores(int idEnrollment, int subjectCode, int score) throws SQLException {
        String sql = "INSERT INTO scores (enrollment_id, subject_id, score) VALUES (?, ?, ?)";

        try (Connection conn = db.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEnrollment);
            stmt.setInt(2, subjectCode);
            stmt.setInt(3, score);

            stmt.executeUpdate();
            System.out.println("Score added for enrollment " + idEnrollment + " and subject " + subjectCode);
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) { // duplicate key
                System.out.println("WARN: Score already exists. Ignoring...");
                return;
            }
            throw e;
        }
    }
}
