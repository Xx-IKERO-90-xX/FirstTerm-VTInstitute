package org.vtinstitute.controller;
import org.hibernate.Session;
import org.vtinstitute.connection.Database;
import org.vtinstitute.models.Subject;
import org.vtinstitute.tools.HibernateUtils;

import java.util.ArrayList;
import java.util.List;

public class SubjectController {
    private Database db = new Database();

    // Function that gives a Subject by code from Database.
    public Subject getSubjectByCode(int code) {
        Session session = HibernateUtils.getSession();
        String hql = "FROM Subject WHERE id = :code";
        try {
            return session.createQuery(hql, Subject.class)
                    .setParameter("code", code)
                    .uniqueResult();

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            session.close();
        }
    }

    // Function that returns subjects for a course and for a year (First or Second year).
    public List<Subject> getCourseSubject(int course, int year) {
        Session session = HibernateUtils.getSession();
        String sql = null;
        try {
            if (year == 1) {
                sql = """
                    SELECT s.code, s.name, s.year
                    FROM subject_courses sc
                    INNER JOIN subjects s ON sc.subject_id = s.code
                    WHERE sc.course_id = :courseId AND s.year = 1
                """;
            } else {
                sql = """
                    SELECT s.code, s.name, s.year
                    FROM subject_courses sc
                    INNER JOIN subjects s ON sc.subject_id = s.code
                    WHERE sc.course_id = :courseId AND s.year = 2
                """;
            }

            List<Object[]> results = session.createNativeQuery(sql)
                    .setParameter("courseId", course)
                    .getResultList();

            List<Subject> subjects = new ArrayList<>();

            for (Object[] row : results) {
                Subject subject = new Subject();
                subject.setId((Integer) row[0]);
                subject.setName((String) row[1]);
                subject.setYear((Integer) row[2]);
                subjects.add(subject);
            }
            return subjects;

        } finally {
            session.close();
        }

    }
}
