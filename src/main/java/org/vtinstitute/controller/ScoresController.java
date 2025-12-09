package org.vtinstitute.controller;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.vtinstitute.models.Enrollment;
import org.vtinstitute.models.Score;
import org.vtinstitute.models.Subject;
import org.vtinstitute.tools.HibernateUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoresController {
    private EnrollmentController enrollmentController = new EnrollmentController();
    private SubjectController subjectController = new SubjectController();
    private LogsController logsController = new LogsController();

    // Function that give us every passed subjects.
    public List<Map<String, Object>> getPassedSubjects(Integer idEnrollment) {

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            logsController.logInfo("Getting passed subjects from enrollment " + idEnrollment);
            String sql = "SELECT * FROM subjects_passed_IRH_2526(:idEnr)";

            NativeQuery<Object[]> query = session.createNativeQuery(sql);
            query.setParameter("idEnr", idEnrollment);

            List<Object[]> rows = query.getResultList();
            List<Map<String, Object>> resultList = new ArrayList<>();

            for (Object[] row : rows) {
                Map<String, Object> map = new HashMap<>();

                map.put("subject", row[0]);
                map.put("score",   row[1]);
                map.put("year",    row[2]);

                resultList.add(map);
            }

            return resultList;
        } catch (Exception e) {
            logsController.logError(e.getMessage());
            System.err.println("There was an error getting passed subjects by enrollment " + idEnrollment);
            return null;
        }
    }


    // Function that give us every not passed subjects.
    public List<Map<String, Object>> getNotPassedSubjects(Integer idEnrollment) {

        String sql = "SELECT * FROM subjects_not_passed_IRH_2526(:idEnr)";

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            logsController.logInfo("Getting not passed subjects from enrollment " + idEnrollment);

            NativeQuery<Object[]> query = session.createNativeQuery(sql);
            query.setParameter("idEnr", idEnrollment);

            List<Object[]> rows = query.getResultList();
            List<Map<String, Object>> resultList = new ArrayList<>();

            for (Object[] row : rows) {
                Map<String, Object> map = new HashMap<>();

                map.put("subject", row[0]);
                map.put("score",   row[1]);
                map.put("year",    row[2]);

                resultList.add(map);
            }

            return resultList;
        } catch (Exception e) {
            System.err.println("There was an error getting not passed subjects by enrollment " + idEnrollment);
            logsController.logError(e.getMessage());

            return null;
        }
    }

    // Function that give us every not passed subjects.
    public List<Map<String, Object>> getScoresByEnrollment(int enrollmentId) {

        String sql = "SELECT * FROM getEnrollmentScoresIJRH(:id)";

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            logsController.logInfo("Getting scores by enrollment " + enrollmentId);

            NativeQuery<Object[]> query = session.createNativeQuery(sql);
            query.setParameter("id", enrollmentId);

            List<Object[]> rows = query.getResultList();
            List<Map<String, Object>> resultList = new ArrayList<>();

            for (Object[] row : rows) {
                Map<String, Object> map = new HashMap<>();

                map.put("subject", row[0]);
                map.put("score",   row[1]);
                map.put("year",    row[2]);

                resultList.add(map);
            }

            return resultList;
        } catch  (Exception e) {
            logsController.logError(e.getMessage());
            System.err.println("There was an error trying to get enrollment scores by enrollment " + enrollmentId);
            return null;
        }
    }

    // Function that adds new Scores to the database.
    public void addNewScores(int idEnrollment, int subjectCode, int mark) {
        Session session = HibernateUtils.getSession();
        Transaction tx = null;

        try {
            logsController.logInfo("Adding new scores for enrollment " + idEnrollment + ", subject code " + subjectCode);
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
          logsController.logError(e.getMessage());
          System.err.println("There was an error while trying to add new scores.");
        } finally {
            session.close();
        }
    }

    // Function that updates scores.
    public void updateScore(int enrollmentId, int subjectId, int newScore) {
        Transaction tx = null;
        Session session = HibernateUtils.getSession();

        try {
            logsController.logInfo("Updating score for enrollment " + enrollmentId + " and subject " + subjectId);
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
            logsController.logError(e.getMessage());
            System.err.println("There was an error trying to update the scores!");

        } finally {
            if (session.isOpen()) session.close();
        }
    }
}
