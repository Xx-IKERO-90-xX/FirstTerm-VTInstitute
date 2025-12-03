package org.vtinstitute.controller;
import org.vtinstitute.connection.Database;
import org.vtinstitute.models.Subject;

import java.util.List;

public class SubjectController {
    private Database db = new Database();

    public List<Subject> getFirstYearSubjects(int course) {
        String sql = "SELECT * FROM subject_courses INNER JOIN subjects ON subject_courses.subject_id = subjects.code WHERE subject_courses.course_id = ? and year = 1";

        try (var conn = db.openConnection();
             var stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, course);

            try (var rs = stmt.executeQuery()) {
                List<Subject> subjects = new java.util.ArrayList<>();

                while (rs.next()) {
                    Subject subject = new Subject();
                    subject.setId(rs.getInt("code"));
                    subject.setName(rs.getString("name"));
                    subject.setYear(rs.getInt("year"));

                    subjects.add(subject);
                }
                return subjects;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving subjects for course " + course, e);
        }
    }

    // Function that allow us to get subjects from second course.
    public List<Subject> getSecondYearSubjects(int course) {
        String sql = "SELECT * FROM subject_courses INNER JOIN subjects ON subject_courses.subject_id = subjects.code WHERE subject_courses.course_id = ? and year = 2";

        try (var conn = db.openConnection();
             var stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, course);

            try (var rs = stmt.executeQuery()) {
                List<Subject> subjects = new java.util.ArrayList<>();

                while (rs.next()) {
                    Subject subject = new Subject();
                    subject.setCode(rs.getInt("code"));
                    subject.setName(rs.getString("name"));
                    subject.setYear(rs.getInt("year"));

                    subjects.add(subject);
                }
                return subjects;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving subjects for course " + course, e);
        }
    }
}
