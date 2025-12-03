package org.vtinstitute.controller;

import org.hibernate.Session;
import org.vtinstitute.connection.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.vtinstitute.models.Cours;
import org.vtinstitute.tools.HibernateUtils;

public class CourseController {

    public Database db = new Database();

    // Checks if a course exists by code.
    public boolean courseExists(int code) {
        Session session = HibernateUtils.getSession();
        try {
            String hql = "FROM Cours WHERE id = :code";
            Integer result = session.createQuery(hql, Cours.class)
                    .setParameter("code", code)
                    .setMaxResults(1)
                    .uniqueResult().getId();

            return result != null;
        } finally {
            session.close();
        }
    }
}
