package org.vtinstitute.controller;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.vtinstitute.models.Cours;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.vtinstitute.connection.Database;
import org.vtinstitute.tools.HibernateUtils;
import org.vtinstitute.controller.LogsController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseController {
    private LogsController logsController = new LogsController();

    // Checks if a course exists by code in the DB.
    public boolean courseExists(int code) throws SQLException {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            logsController.logInfo("Checking if course exists for code " + code);
            return session.createQuery("FROM Cours c WHERE c.id = :code", Cours.class)
                    .setParameter("code", code)
                    .uniqueResultOptional()
                    .isPresent();
        }
    }

    // Function that gives a Course from the DB
    public Cours getCourse(int code) {
        Session session = HibernateUtils.getSession();
        try {
            logsController.logInfo("Getting course by " + code + " from DB");
            String hql = "FROM Cours c WHERE c.id = :code";
            return session.createQuery(hql, Cours.class)
                    .setParameter("code", code)
                    .getSingleResult();

        } catch (NoResultException e) {
            logsController.logError(e.getMessage());
            System.err.println("No course with code " + code);
            return null;
        } finally {
            session.close();
        }
    }
}
