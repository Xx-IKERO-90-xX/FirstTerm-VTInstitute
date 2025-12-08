package org.vtinstitute.controller;

import org.vtinstitute.models.Enrollment;
import org.vtinstitute.models.Student;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class PrintController {
    private EnrollmentController enrollmentController = new EnrollmentController();
    private ScoresController scoresController = new ScoresController();
    private StudentsController studentController = new StudentsController();
    private LogsController logsController = new LogsController();

    // Function that prints the entire expedient from a Student.
    public void printExpedient(String idCard) throws SQLException {
        if (!studentController.studentExists(idCard)) {
            System.err.println("Student does not exist.");
            return;
        }

        Student student = studentController.getStudent(idCard);
        System.out.println("Expedient from " + student.getFirstname() + " (" + idCard + ")");
        System.out.println("================================================");

        List<Enrollment> enrollments = enrollmentController.getStudentEnrollments(idCard);

        for (Enrollment enrollment : enrollments) {
            System.out.println("Year: " + enrollment.getYear());

            List<Map<String,Object>> scores = scoresController.getScoresByEnrollment(enrollment.getId());

            for (var row : scores) {
                String subjectName = (String) row.get("subject");
                int score = (int) row.get("score");

                System.out.println(" - " + subjectName + ": " + score);
            }

            System.out.println();
        }
    }

    // Function that prints an expedient to a TXT file.
    public void printExpedientTXT(String idCard) throws SQLException {
        if (!studentController.studentExists(idCard)) {
            System.err.println("Student does not exist.");
            return;
        }

        // Creates file exports it's not exists.
        File exportDir = new File("exports");
        if (!exportDir.exists()) {
            logsController.logInfo("Creating file /exports.");
            exportDir.mkdirs();
        }

        String fileName = "exports/expedient_" + idCard + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            Student student = studentController.getStudent(idCard);

            writer.write("Expedient from " + student.getFirstname() + " (" + idCard + ")");
            writer.newLine();
            writer.write("================================================");
            writer.newLine();
            writer.newLine();

            List<Enrollment> enrollments = enrollmentController.getStudentEnrollments(idCard);

            for (Enrollment enrollment : enrollments) {
                writer.write("Year: " + enrollment.getYear());
                writer.newLine();

                List<Map<String, Object>> scores =
                        scoresController.getScoresByEnrollment(enrollment.getId());

                for (var row : scores) {
                    String subjectName = (String) row.get("subject");
                    int score = (int) row.get("score");

                    writer.write(" - " + subjectName + ": " + score);
                    writer.newLine();
                }

                writer.newLine();
            }
            System.out.println("Expedient exported to: " + fileName);
            logsController.logInfo("Successfully exported " + fileName);

        } catch (IOException e) {
            logsController.logError(e.getMessage());
            System.err.println("There was an error exporting " + fileName);
        }
    }
}
