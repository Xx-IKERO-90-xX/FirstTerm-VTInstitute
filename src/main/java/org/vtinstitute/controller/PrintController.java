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
}
