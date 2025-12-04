package org.vtinstitute.controller;

import org.vtinstitute.models.Enrollment;
import org.vtinstitute.models.Student;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class PrintController {
    private EnrollmentController enrollmentController = new EnrollmentController();
    private ScoresController scoresController = new ScoresController();
    private StudentsController studentController = new StudentsController();

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
}
