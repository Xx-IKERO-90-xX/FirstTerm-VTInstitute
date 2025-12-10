package org.vtinstitute;

import org.vtinstitute.controller.*;
import org.vtinstitute.models.Enrollment;
import org.vtinstitute.models.Subject;

import java.sql.SQLException;
import java.time.LocalDate;
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
            --add -a {file.xml} : Add students from the specified XML file.
            --enroll -e {StudentCard} {CourseId} {year}: Matriculate a student to a course.
            --qualify -q {enrollemntId} {subjectId} {0-10}: Qualifies a subject for a enrollment.
            --print -p {StudentCard} --options : Shows the expedient from a student, options are optionals and the command without options prints only by terminal.
                Options: 
                    --file -f: Exports an expedients to a TXT file in the route /exports.
            --close -c : Closes actual courses.
                Options:
                    --force -f: Forces to close actual courses.  
             
            =======================
        """);
    }
    public static void main(String[] args) throws SQLException {

        switch (args[0]){
            case "--help", "-h" -> {
                showDocumentation();
            }
            case "--add", "-a" -> {
                if (args.length > 2) {
                    System.err.println("Too many arguments.");
                    return;
                }
                if (args.length < 2) {
                    System.err.println("There are not enough arguments.");
                    System.out.println("Usage: --add {file.xml}");
                    return;
                }

                String xmlFile = args[1];
                studentsController.addStudentsXML(xmlFile);
            }

            case "--enroll", "-e" -> {
                if (args.length < 4) {
                    System.err.println("There are not enough arguments.");
                    System.out.println("Usage: --enroll [studentCard] [courseId] [year]");
                    return;
                }

                String studentCard = args[1];
                int courseId = 0;
                int year = 0;

                // We validate if courseId and year are numerics.
                try {
                    courseId = parseInt(args[2]);
                    year = parseInt(args[3]);
                } catch (Exception e) {
                    System.err.println("Course id and year must be numeric.");
                    return;
                }

                if (studentsController.studentExists(studentCard) && courseController.courseExists(courseId)){
                    if (enrollmentController.isFirstEnrollment(studentCard)) {
                        // Get subjects of first year
                        List<Subject> subjectsFirstYear = subjectController.getCourseSubject(courseId, 1);

                        // Create the enrollment
                        enrollmentController.enrollStudent(studentCard, courseId, year);

                        // Adds Subject initial scores for the new enrollment.
                        Enrollment enrollment = enrollmentController.getLastStudentEnrollment(studentCard);

                        for (Subject subject : subjectsFirstYear) {
                            scoresController.addNewScores(enrollment.getId(), subject.getId(), 0);
                        }
                    } else {
                        // Gets second year subjects from a course.
                        List<Subject> subjectsSecondYear = subjectController.getCourseSubject(courseId, 2);

                        // Creates the second enrollment.
                        enrollmentController.enrollStudent(studentCard, courseId, year);

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
                    System.err.println("There are not enough arguments.");
                    System.out.println("Usage: --qualify [enrollmentId] [subjectId] [0-10]");
                } else {
                    int enrollment = 0;
                    int subject = 0;
                    int score = 0;

                    try {
                        enrollment = parseInt(args[2]);
                        subject = parseInt(args[3]);
                        score = parseInt(args[4]);
                    } catch (Exception e){
                        System.err.println("Enrollment id, subject id and score must be numeric.");
                        return;
                    }

                    if (score < 0 || score > 10) {
                        System.err.println("Score must be between 0 and 10.");
                        return;
                    }

                    scoresController.updateScore(enrollment, subject, score);
                }
            }

            case "--print", "-p" -> {
                if (args.length < 3 ) {
                    System.err.println("There are not enough arguments.");
                    System.out.println("Usage: --print {studentIdCard} {option}");
                    return;
                }
                if (args.length > 4) {
                    System.err.println("Too many arguments.");
                    System.out.println("Usage: --print {studentIdCard} {option}");
                    return;
                }

                String idCard = args[1];
                int idCourse = 0;

                // We check if the idCourse is a number or letter.
                try {
                    idCourse = parseInt(args[2]);
                } catch (Exception e) {
                    System.err.println("Course id must be numeric.");
                    return;
                }

                // We check if student and course exists.
                if (!studentsController.studentExists(idCard)){
                    System.err.println("The student with id " + idCard + " does not exist in the database.");
                    return;
                }
                if (!courseController.courseExists(idCourse)){
                    System.err.println("Course with id " + idCourse + " does not exist in the database.");
                    return;
                }

                // We get the student enrollments from a specific cours.
                List<Enrollment> enrollments = enrollmentController.getStudentEnrollmentsCourse(idCard, idCourse);
                if (enrollments.isEmpty()) {
                    System.err.println("There are no enrolled students in the database to the course " + idCourse + ".");
                    return;
                }

                if (args.length == 4) {
                    switch (args[3]) {
                        case "--file", "-f" -> {
                            printController.printExpedientTXT(idCard, idCourse, enrollments);
                            return;
                        }
                        default -> {
                            System.err.println("Please enter a valid option.");
                            System.out.println("Usage: --print {studentIdCard} {option}");
                            return;
                        }
                    }
                }

                printController.printExpedient(enrollments);
            }
            case "-c", "--close" -> {
                if (args.length < 2) {
                    System.err.println("There are not enough arguments.");
                    System.out.println("Usage: --close {options}");
                    return;
                }
                if (args.length > 2) {
                    switch (args[2]) {
                        case "--force", "-f" -> {
                            scoresController.closeActualCourses();
                            return;
                        }
                        default -> {
                            System.err.println("Please enter a valid option.");
                            System.out.println("Usage: --close {options}");
                            return;
                        }
                    }
                }
                LocalDate actualDate = LocalDate.now();

                if (actualDate.getMonthValue() == 7 || actualDate.getMonthValue() == 8 || actualDate.getMonthValue() == 9 ) {
                    scoresController.closeActualCourses();
                } else {
                    System.err.println("Actually you cannot to close actual courses now.");
                }
            }
        }
    }
}