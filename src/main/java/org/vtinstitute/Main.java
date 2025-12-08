package org.vtinstitute;

import org.vtinstitute.controller.*;
import org.vtinstitute.models.Enrollment;
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
    private static PrintController printController = new  PrintController();

    public static void showDocumentation() {
        System.out.println("""
            VTInstitute Application
            =======================
            --help -h : Show this documentation.
            --add -a : Add students from the specified XML file.
            --enroll -e [StudentCard] [Course] [year]: Matriculate a student to a course.
            --qualify -q [enrollemntId] [subjectId] [0-10]: Qualifies a subject for a enrollment.
            --print -p [StudentCard] --options : Shows the expedient from a student, options are optionals and the command without options prints only by terminal.
                Options: 
                    -txt: Exports an expediento to a TXT file in the route /exports.
            =======================
        """);
    }
    public static void main(String[] args) throws SQLException {

        switch (args[0]){
            case "--help", "-h" -> {
                showDocumentation();
            }
            case "--add", "-a" -> {
                if (args.length > 1) {
                    System.err.println("Too many arguments.");
                    return;
                }
                studentsController.addStudentsXML();
                break;
            }

            case "--enroll", "-e" -> {
                if (args.length < 3) {
                    System.err.println("There are not enought arguments.");
                    return;
                }

                String studentCard = args[1];
                int courseId = parseInt(args[2]);

                if (studentsController.studentExists(studentCard) && courseController.courseExists(courseId)){
                    if (enrollmentController.isFirstEnrollment(studentCard)) {
                        // Get subjects of first year
                        List<Subject> subjectsFirstYear = subjectController.getCourseSubject(courseId, 1);

                        // Create the enrollment
                        enrollmentController.enrollStudent(studentCard, courseId);

                        // Adds Subject initial scores for the new enrollment.
                        Enrollment enrollment = enrollmentController.getLastStudentEnrollment(studentCard);

                        for (Subject subject : subjectsFirstYear) {
                            scoresController.addNewScores(enrollment.getId(), subject.getId(), 0);
                        }
                    } else {
                        // Gets second year subjects from a course.
                        List<Subject> subjectsSecondYear = subjectController.getCourseSubject(courseId, 2);

                        // Creates the second enrollment.
                        enrollmentController.enrollStudent(studentCard, courseId);

                        // Get the new enrollment created.
                        Enrollment enrollment = enrollmentController.getLastStudentEnrollment(studentCard);

                        // Adds Subject initial scores for the second year.
                        for (Subject subject : subjectsSecondYear) {
                            scoresController.addNewScores(enrollment.getId(), subject.getId(), 0);
                        }

                        // Get not passed students
                        List<Map<String, Object>> notPassedSubjects = scoresController.getNotPassedSubjects(enrollment.getId());
                        for (Map<String, Object> subjectData : notPassedSubjects) {
                            int subjectCode = (int) subjectData.get("subject");
                            int score = (int) subjectData.get("score");
                            scoresController.addNewScores(enrollment.getId(), subjectCode, score);
                        }
                    }
                } else {
                    System.err.println("Some of records aren't exists in the Database.");
                }
            }
            case "--qualify", "-q" -> {
                if (args.length < 4) {
                    System.err.println("There are not enought arguments.");
                    System.out.println("Usage: --qualify [enrollmentId] [subjectId] [0-10]");
                    return;
                } else {
                    int enrollment = parseInt(args[1]);
                    int subject = parseInt(args[2]);
                    int score = parseInt(args[3]);

                    if (score < 0 || score > 10) {
                        System.err.println("Score must be between 0 and 10.");
                        return;
                    }

                    scoresController.updateScore(enrollment, subject, score);
                }
            }

            case "--print", "-p" -> {
                if (args.length < 2 ) {
                    System.err.println("There are not enough arguments.");
                    return;
                } else if (args.length == 3 && args[2].equals("-txt")) {
                    printController.printExpedientTXT(args[1]);
                } else if (args.length == 2) {
                    printController.printExpedient(args[1]);
                } else {
                    System.err.println("There are not enough arguments.");
                }
            }
        }
    }
}