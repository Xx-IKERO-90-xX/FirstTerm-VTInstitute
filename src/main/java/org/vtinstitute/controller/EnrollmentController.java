package org.vtinstitute.controller;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.vtinstitute.connection.Database;
import org.vtinstitute.models.Cours;
import org.vtinstitute.models.Enrollment;
import org.vtinstitute.controller.LogsController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.vtinstitute.models.Student;
import org.vtinstitute.tools.HibernateUtils;

public class EnrollmentController {
    private StudentsController studentsController = new StudentsController();
    private CourseController courseController = new CourseController();
    private LogsController logsController = new LogsController();

    // Function that tests if the student is enrolled to a Course.
    public boolean isStudentEnrolled(String idCard) {
        Session session = HibernateUtils.getSession();
        try {
            logsController.logInfo("Checking if student " + idCard + " is enrolled");
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
            logsController.logInfo("Checking if student " + idCard + " is enrolled by the first time");
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
            logsController.logInfo("Enrolling student " + idCard + " to course " + course);
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
            logsController.logError(e.getMessage());
            System.err.println("Failed to enroll student " + idCard + " to course " + course);
        }
    }

    // Gets an enrollment by a code.
    public Enrollment getEnrollmentByCode(int code) {
        Session session = HibernateUtils.getSession();
        Enrollment enrollment = null;
        try {
            logsController.logInfo("Getting enrollment by code " + code);
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
            logsController.logInfo("Getting last student enrollment by code " + idCard);
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
            logsController.logInfo("Getting student enrollment by code " + idCard);
            String hql = "FROM Enrollment e WHERE e.student.idcard = :idCard ORDER BY e.year ASC";
            return session.createQuery(hql, Enrollment.class)
                    .setParameter("idCard", idCard)
                    .list();
        } finally {
            session.close();
        }
    }
}
