package org.vtinstitute.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vtinstitute.controller.*;
import org.vtinstitute.models.entity.Score;
import org.vtinstitute.models.entity.Student;
import org.vtinstitute.models.entity.Enrollment;
import org.vtinstitute.models.ApiManager;

public class EnrollmentController {
    private LogsController logsController;
    private ApiManager apiManager;

    public void enrollStudentApi(String idcard, int idcours, int year) throws IOException {
        Map<String, Object> enrollRequest = new HashMap();
        enrollRequest.put("idcard", idcard);
        enrollRequest.put("idcours", idcours);
        enrollRequest.put("year", year);
        apiManager.enrollStudentApi(enrollRequest);
    }

    public List<Enrollment> getEnrollmentsByStudentCours(String idcard, int idcours) throws IOException {
        List<Enrollment> enrollments = apiManager.getEnrollmentsByStudentCours(idcard, idcours);
        return enrollments;
    }
}
