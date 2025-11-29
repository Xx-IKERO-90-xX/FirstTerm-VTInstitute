package org.vtinstitute.controller;
import org.vtinstitute.models.Course;

import org.vtinstitute.connection.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseController {

    public Database db = new Database();

    // Checks if a course exists by code.
    public boolean courseExists(int code) throws SQLException {
        Database db = new Database();

        String sql = "SELECT 1 FROM courses WHERE code = ?";

        Connection conn = db.openConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, code);

        ResultSet rs = stmt.executeQuery();
        conn.close();

        return rs.next();
    }

    // Function that updates scores.
    public void  updateScore(int enrollmentId, int subjectId, int newScore) {
        String sql = "UPDATE scores SET score = ? WHERE enrollment_id = ? AND subject_id = ?";

        try (Connection conn = db.openConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, newScore);
            stmt.setInt(2, enrollmentId);
            stmt.setInt(3, subjectId);

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Subject not found in this enrollment.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
