package org.vtinstitute.controller;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.vtinstitute.connection.Database;
import org.vtinstitute.models.Cours;
import org.vtinstitute.models.Enrollment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.vtinstitute.models.Student;
import org.vtinstitute.tools.HibernateUtils;

public class EnrollmentController {
    private Database db = new Database();
    private StudentsController studentsController = new StudentsController();
    private CourseController courseController = new CourseController();

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
            Long count = session.createQuery(
                    "SELECT COUNT(e) FROM Enrollment e WHERE e.student.idcard = :idCard",
                    Long.class
            ).setParameter("idCard", idCard).uniqueResult();

            return count == 0;
        } finally {
            session.close();
        }
    }


    // Function that enrolls a Student to a course.
    public void enrollStudent(String idCard, int course) {
        Transaction tx = null;

        try (Session session = HibernateUtils.getSession()) {
            tx = session.beginTransaction();

            Student student = studentsController.getStudent(idCard);
            Cours cours = courseController.getCourse(course);
            Enrollment enrollment = new Enrollment();
            enrollment.setStudent(student);
            enrollment.setCourse(cours);
            enrollment.setYear(2025);

            session.persist(enrollment);
            tx.commit();

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error rolling student.", e);
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
        String hql = "FROM Enrollment e WHERE e.student.idcard = :student ORDER BY e.year DESC";

        try {
            return session.createQuery(hql, Enrollment.class)
                    .setParameter("student", idCard)
                    .setMaxResults(1)
                    .uniqueResult();
        } finally {
            session.close();
        }
    }

    // Function that get enrollments from a student.
    public List<Enrollment> getStudentEnrollments(String idCard) {
        Session session = HibernateUtils.getSession();
        try {
            String hql = "FROM Enrollment e WHERE e.student.idcard = :idCard ORDER BY e.year ASC";
            return session.createQuery(hql, Enrollment.class)
                    .setParameter("idCard", idCard)
                    .list();
        } finally {
            session.close();
        }
    }
}
