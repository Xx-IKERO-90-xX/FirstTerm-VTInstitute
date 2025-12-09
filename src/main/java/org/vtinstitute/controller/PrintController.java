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

    public void printExpedient(List<Enrollment> enrollments) {

        // Table header
        System.out.printf("%-6s %-35s %-5s%n", "Year", "Subjects", "Score");
        System.out.println("-------------------------------------------------------------");

        for (Enrollment enrollment : enrollments) {
            List<Map<String,Object>> scores = scoresController.getScoresByEnrollment(enrollment.getId());

            for (var row : scores) {
                String subjectName = (String) row.get("subject");
                int score = (int) row.get("score");

                // Table rows
                System.out.printf("%-6d %-35s %-5d%n",
                        enrollment.getYear(),
                        subjectName,
                        score
                );
            }
        }
    }

    // Function that prints a Student expedient to a txtFile.
    public void printExpedientTXT(String idCard, List<Enrollment> enrollments) {
        String exportsFolder = "exports/";
        String fileName = exportsFolder + "expedient_" + idCard + ".txt";

        File folder = new File(exportsFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Header
            writer.write(String.format("%-6s %-35s %-5s%n", "Year", "Subjects", "Score"));
            writer.write("-------------------------------------------------------------\n");

            for (Enrollment enrollment : enrollments) {
                List<Map<String,Object>> scores =
                        scoresController.getScoresByEnrollment(enrollment.getId());

                for (var row : scores) {
                    String subjectName = (String) row.get("subject");
                    int score = (int) row.get("score");

                    writer.write(String.format(
                            "%-6d %-35s %-5d%n",
                            enrollment.getYear(),
                            subjectName,
                            score
                    ));
                }
            }

            System.out.println("Expedient generated: " + fileName);
        } catch (IOException e) {
            logsController.logError(e.getMessage());
            System.err.println("Error writing expedient file: " + fileName);
        }

    }
}
