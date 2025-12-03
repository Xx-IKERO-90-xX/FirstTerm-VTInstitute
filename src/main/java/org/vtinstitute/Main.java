package org.vtinstitute;

import org.vtinstitute.controller.EnrollmentController;
import org.vtinstitute.controller.ScoresController;
import org.vtinstitute.controller.StudentsController;
import org.vtinstitute.controller.CourseController;
import org.vtinstitute.controller.SubjectController;

import org.vtinstitute.models.Enrollment;
import org.vtinstitute.models.Student;
import org.vtinstitute.models.Subject;
import org.vtinstitute.models.Score;
import org.vtinstitute.models.Subject;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class Main {
    private static CourseController courseController = new CourseController();
    private static StudentsController studentsController = new StudentsController();
    private static EnrollmentController enrollmentController = new EnrollmentController();
    private static SubjectController subjectController = new SubjectController();
    private static ScoresController scoresController = new ScoresController();

    public static void showDocumentation() {
        System.out.println("""
            VTInstitute Application
            =======================
            --help : Show this documentation.
            --add students.xml : Add students from the specified XML file.
            --enroll : Matriculate a student to a course.
            --qualify : Qualifies students.
            --print : Shows the expedient from a student.
            =======================
        """);
    }
    public static void main(String[] args) throws SQLException {

        switch (args[0]){
            case "--help" -> {
                showDocumentation();
            }
            case "--add" -> {
                if (args.length > 1) {
                    System.err.println("Too many arguments.");
                    return;
                }
                studentsController.addStudentsXML();
                break;
            }

            case "--enroll" -> {
                if (args.length < 3) {
                    System.err.println("There are not enought arguments.");
                    return;
                }
                if (studentsController.studentExists(args[1]) && courseController.courseExists(Integer.parseInt(args[2]))){
                    if (enrollmentController.isFirstEnrollment(args[1])) {
                        // Get subjects of first year
                        List<Subject> subjectsFirstYear = subjectController.getFirstYearSubjects(parseInt(args[2]));
                        enrollmentController.enrollStudent(args[1], parseInt(args[2]));

                        Enrollment enrollment = enrollmentController.getLastStudentEnrollment(args[1]);
                        for (Subject subject : subjectsFirstYear) {
                            scoresController.addNewScores(enrollment.getId(), subject.getId(), 0);
                        }
                    } else {
                        List<Subject> subjectsSecondYear = subjectController.getSecondYearSubjects(parseInt(args[2]));
                        enrollmentController.enrollStudent(args[1], parseInt(args[2]));

                        Enrollment enrollment = enrollmentController.getLastStudentEnrollment(args[1]);
                        for (Subject subject : subjectsSecondYear) {
                            scoresController.addNewScores(enrollment.getId(), subject.getId(), 0);
                        }

                        List<Map<String, Object>> notPassedSubjects = scoresController.getNotPassedSubjects(enrollment.getId());
                        for (Map<String, Object> subjectData : notPassedSubjects) {
                            int subjectCode = (int) subjectData.get("subject");
                            int score = (int) subjectData.get("score");
                            scoresController.addNewScores(enrollment.getId(), subjectCode, score);
                        }
                    }
                }
                else {
                    System.out.println(studentsController.studentExists(args[1]));
                    System.out.println(courseController.courseExists(parseInt(args[2])));
                    System.err.println("Some of records aren't exists in the Dabasase, review the data and try again.");
                }
            }
            case "--qualify" -> {
                if (args.length < 4) {
                    System.err.println("There are not enought arguments.");
                    return;
                }
                else {
                    int enrollment = parseInt(args[1]);
                    int subject = parseInt(args[2]);
                    int score = parseInt(args[3]);

                    scoresController.updateScore(enrollment, subject, score);
                }

            }
        }
    }
}