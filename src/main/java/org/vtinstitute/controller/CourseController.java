package org.vtinstitute.controller;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.vtinstitute.models.Cours;

import org.vtinstitute.connection.Database;
import org.vtinstitute.tools.HibernateUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseController {

    public Database db = new Database();

    // Checks if a course exists by code in the DB.
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

    // Function that gives a Course from the DB
    public Cours getCourse(int code) {
        Session session = HibernateUtils.getSession();
        try {
            String hql = "FROM Cours c WHERE c.id = :code";
            return session.createQuery(hql, Cours.class)
                    .setParameter("code", code)
                    .getSingleResult();

        } catch (NoResultException e) {
            System.err.println("No course with code " + code);
            return null;
        } finally {
            session.close();
        }
    }
}
