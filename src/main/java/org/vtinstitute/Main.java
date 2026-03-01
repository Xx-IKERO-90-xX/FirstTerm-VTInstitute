package org.vtinstitute;

import org.vtinstitute.controller.*;
import org.vtinstitute.models.entity.Enrollment;

import java.sql.SQLException;
import java.util.List;

import static java.lang.Integer.parseInt;

import java.io.IOException;

public class Main {
    private static StudentsController studentsController = new StudentsController();
    private static EnrollmentController enrollmentController = new EnrollmentController();
    private static PrintController printController = new PrintController();
    private static ScoreController scoreController = new ScoreController();

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
    public static void main(String[] args) throws SQLException, IOException {

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

                enrollmentController.enrollStudentApi(studentCard, courseId, year);
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

                    // Aquí va el código.
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
                List<Enrollment> enrollments = enrollmentController.getEnrollmentsByStudentCours(idCard, idCourse);
                printController.printExpedient(enrollments);
                // Aquí va el codigo 
            }
            /** 
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
            **/
        }
    }
}