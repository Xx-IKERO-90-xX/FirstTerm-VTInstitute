package org.vtinstitute.controller;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.vtinstitute.connection.Database;
import org.vtinstitute.models.Enrollment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.vtinstitute.models.Student;
import org.vtinstitute.tools.HibernateUtils;

public class EnrollmentController {
    private Database db = new Database();

    // Function that tests if the student is enrolled to a Course.
    public boolean isStudentEnrolled(String idCard) {
        Session session = HibernateUtils.getSession();
        try {
            String hql = "FROM Enrollment e WHERE e.student = :idCard";
            Query<Enrollment> query = session.createQuery(hql, Enrollment.class);
            query.setParameter("idCard", idCard);

            return !query.getResultList().isEmpty();
        } finally {
            session.close();
        }
    }


    // Checks if is the first enrollment or not.
    public boolean isFirstEnrollment(String idCard) {
        Session session = HibernateUtils.getSession();
        try {
            // 1️⃣ Obtener el estudiante
            Student student = session.get(Student.class, idCard); // asumiendo que tu ID es idCard
            if (student == null) return true; // No existe, por lo que es "primera inscripción"

            // 2️⃣ Contar enrollments
            String hql = "SELECT COUNT(e) FROM Enrollment e WHERE e.student = :student";
            Long count = session.createQuery(hql, Long.class)
                    .setParameter("student", student)
                    .uniqueResult();

            return count == 0;
        } finally {
            session.close();
        }
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
        Session session = HibernateUtils.getSession();
        Enrollment enrollment = null;
        try {
            String hql = "FROM Enrollment e WHERE e.id = :code";
            enrollment = session.createQuery(hql, Enrollment.class)
                    .setParameter("code", code)
                    .setMaxResults(1)
                    .uniqueResult();

        } finally {
            session.close();
        }
        return enrollment;
    }

    // Function that gets the last enrollment of a student.
    public Enrollment getLastStudentEnrollment(String idCard) {
        Session session = HibernateUtils.getSession();
        String hql = "FROM Enrollment e WHERE e.student = :student ORDER BY e.year DESC";

        try {
            return session.createQuery(hql, Enrollment.class)
                    .setParameter("student", idCard)
                    .setMaxResults(1)
                    .uniqueResult();
        } finally {
            session.close();
        }
    }
}
