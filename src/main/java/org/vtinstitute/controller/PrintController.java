package org.vtinstitute.controller;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.vtinstitute.models.ApiManager;
import org.vtinstitute.models.entity.Enrollment;
import org.vtinstitute.models.entity.Score;
import org.vtinstitute.controller.LogsController;

public class PrintController {
    private ApiManager apiManager;
    private LogsController logsController;

    public void printExpedient(List<Enrollment> enrollments) throws IOException {

        // Table header
        System.out.printf("%-6s %-35s %-5s%n", "Year", "Subjects", "Score");
        System.out.println("-------------------------------------------------------------");

        for (Enrollment enrollment : enrollments) {
            String idCard = enrollment.getStudent().getIdcard();
            String cours = String.valueOf(enrollment.getCourse().getId());

            List<Score> scores = apiManager.getStudentExpedient(idCard, cours);

            for (Score row : scores) {
                String subjectName = row.getEnrollment().getCourse().getName();
                int score = row.getScore();

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
    public void printExpedientTXT(String idCard, int idCours, List<Enrollment> enrollments) {
        String exportsFolder = "exports/";
        String fileName = exportsFolder + idCard + "-" + idCours + ".txt";

        File folder = new File(exportsFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Header
            writer.write(String.format("%-6s %-35s %-5s%n", "Year", "Subjects", "Score"));
            writer.write("-------------------------------------------------------------\n");

            for (Enrollment enrollment : enrollments) {
                List<Score> scores = apiManager.getStudentExpedient(idCard, fileName);

                for (Score row : scores) {
                    String subjectName = row.getSubject().getName();
                    int score = row.getScore();

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
