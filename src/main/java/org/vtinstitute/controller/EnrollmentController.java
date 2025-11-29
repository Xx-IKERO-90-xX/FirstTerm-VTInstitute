package org.vtinstitute.controller;

import org.vtinstitute.models.Enrollment;
import org.vtinstitute.connection.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnrollmentController {
    private Database db = new Database();

    // Function that tests if the student is enrolled to a Course.
    public boolean isStudentEnrolled(String idCard) throws SQLException {
        String sql = "SELECT COUNT(*) FROM enrollments WHERE idcard = ?";

        Connection conn = db.openConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, idCard);

        ResultSet rs = stmt.executeQuery();
        conn.close();

        return rs.next();
    }


    // Checks if is the first enrollment or not.
    public boolean isFirstEnrollment(String idCard) {
        String sql = "SELECT COUNT(*) FROM enrollments WHERE student = ?";

        try {
            Connection conn = db.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, idCard);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count == 0;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error checking first enrollment", e);
        }
        return false;
    }


    // Function that enrolls a Student to a course.
    public void enrollStudent(String idCard, int course) {
        String sql = "INSERT INTO enrollments (student, course, year) VALUES (?, ?, ?)";

        try (Connection conn = db.openConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, idCard);
            stmt.setInt(2, course);
            stmt.setInt(3, 2025);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Gets an enrollment by a code.
    public Enrollment getEnrollmentByCode(int code) {
        String sql = "SELECT * FROM enrollments WHERE code = ?";
        Enrollment enrollment = null;

        try (Connection conn = db.openConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, code);

            var rs = stmt.executeQuery();

            if (rs.next()) {
                enrollment = new Enrollment(
                        rs.getInt("code"),
                        rs.getString("student"),
                        rs.getInt("course"),
                        rs.getInt("year")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return enrollment;
    }

    // Function that gets the last enrollment of a student.
    public Enrollment getLastStudentEnrollment(String idCard) {
        String sql = "SELECT * FROM enrollments WHERE student = ? ORDER BY year DESC LIMIT 1";
        Enrollment enrollment = null;

        try (Connection conn = db.openConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, idCard);

            var rs = stmt.executeQuery();

            if (rs.next()) {
                enrollment = new Enrollment(
                        rs.getInt("code"),
                        rs.getString("student"),
                        rs.getInt("course"),
                        rs.getInt("year")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return enrollment;    
    }
}
