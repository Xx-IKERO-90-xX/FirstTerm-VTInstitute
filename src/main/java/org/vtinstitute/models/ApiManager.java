package org.vtinstitute.models;

import java.util.List;
import java.util.Map;

import org.vtinstitute.utils.HttpResponse;

import java.io.IOException;

import org.vtinstitute.controller.LogsController;
import org.vtinstitute.models.entity.Enrollment;
import org.vtinstitute.models.entity.Score;
import org.vtinstitute.models.entity.Student;
import org.vtinstitute.utils.RestApiConnection;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiManager {

    private LogsController logsController = new LogsController();
    private RestApiConnection restApiConnection;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Student getStudent(String idcard) throws IOException {
        JavaType type = objectMapper.getTypeFactory().constructType(Student.class);
        HttpResponse response = restApiConnection.getRequest("/students", idcard, null);
        return objectMapper.convertValue(response, type);
    }

    public List<Score> getStudentExpedient(String idcard, String cours) throws IOException {
        JavaType type = objectMapper.getTypeFactory()
            .constructCollectionType(List.class, Score.class);

        HttpResponse response = restApiConnection.getRequest("/students/expedient/", idcard, cours);
        Object responseBody = response.getBody(); 

        return objectMapper.convertValue(responseBody, type);
    }

    public List<Enrollment> getEnrollmentsByStudentCours(String idcard, int cours) throws IOException {
        JavaType type = objectMapper.getTypeFactory()
            .constructCollectionType(List.class, Enrollment.class);
        
        HttpResponse response = restApiConnection.getRequest("/enrollments/", idcard, String.valueOf(cours));
        Object responseBody = response.getBody();
        return objectMapper.convertValue(responseBody,type);
    }

    public int addStudentAPI(Student student) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(student);
        } catch(Exception e) {
            System.err.println("The data isn't imported to JSON correctly");
            logsController.logError(e.getMessage());
        }
        HttpResponse response = restApiConnection.post("/students/add", json);
        return response.getStatusCode();
    }

    public void enrollStudentApi(Map<String, Object> enrollRequest) throws IOException {
        String jsonPayload = objectMapper.writeValueAsString(enrollRequest);
        HttpResponse httpResponse = restApiConnection.post("/students/enroll", jsonPayload);
        
        if (httpResponse.getStatusCode() == 200 || httpResponse.getStatusCode() == 201) {
            System.out.println("The student was enrolled successed");
        } else {
            System.err.print("There was an error enrolling the student.");
        }
    }

    public void qualifyApi(Map<String, Object> newScore) throws IOException {
        String jsonPayload = objectMapper.writeValueAsString(newScore);
        HttpResponse httpResponse = restApiConnection.put("/scores/qualify", jsonPayload);

        if (httpResponse.getStatusCode() == 200 || httpResponse.getStatusCode() == 201) {
            System.out.println("The score was updated successfully!!!");
        } else {
            System.err.println("There was an error qualifying.");
        }
    }
}
