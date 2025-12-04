package org.vtinstitute.controller;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.vtinstitute.connection.Database;
import org.vtinstitute.models.Enrollment;
import org.vtinstitute.models.Score;
import org.vtinstitute.models.Subject;
import org.vtinstitute.tools.HibernateUtils;
import org.vtinstitute.controller.EnrollmentController;

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
    private EnrollmentController enrollmentController = new EnrollmentController();
    private SubjectController subjectController = new SubjectController();

    // Function that give us every passed subjects.
    public List<Map<String, Object>> getPassedSubjects(Integer idEnrollment) {
        String sql = "SELECT * FROM subjects_passed_IRH_2526(?)";
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

    // Function that give us every not passed subjects.
    public List<Map<String, Object>> getScoresByEnrollment(int code) {
        String sql = "SELECT * from getEnrollmentScoresIJRH(?)";
        List<Map<String, Object>> scores = new ArrayList<>();

        try (Connection conn = db.openConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, code);
            var rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> result = new HashMap<>();
                result.put("subject", rs.getString("subject"));
                result.put("score", rs.getInt("score"));
                result.put("year", rs.getInt("year"));

                scores.add(result);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return scores;
    }

    // Function that adds new Scores to the database.
    public void addNewScores(int idEnrollment, int subjectCode, int mark) {
        Session session = HibernateUtils.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            Enrollment enrollment = enrollmentController.getEnrollmentByCode(idEnrollment);
            Subject subject = subjectController.getSubjectByCode(subjectCode);

            Score score = new Score();
            score.setEnrollment(enrollment);
            score.setSubject(subject);
            score.setScore(mark);

            session.persist(score);
            tx.commit();

            System.out.println("Score added for enrollment " + idEnrollment + " and subject " + subjectCode);
        } catch (Exception e) {
          if (tx != null) tx.rollback();
          throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    // Function that updates scores.
    public void updateScore(int enrollmentId, int subjectId, int newScore) {
        Transaction tx = null;
        Session session = HibernateUtils.getSession();

        try {
            tx = session.beginTransaction();

            String hql = """
                UPDATE Score s 
                SET s.score = :score 
                WHERE s.enrollment.id = :enrollmentId 
                AND s.subject.id = :subjectId
            """;

            session.createMutationQuery(hql)
                    .setParameter("score", newScore)
                    .setParameter("enrollmentId", enrollmentId)
                    .setParameter("subjectId", subjectId)
                    .executeUpdate();


            tx.commit();

            System.out.println("Score updated for enrollment " + enrollmentId + " and subject " + subjectId);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException(e);
        } finally {
            if (session.isOpen()) session.close();
        }
    }
}
