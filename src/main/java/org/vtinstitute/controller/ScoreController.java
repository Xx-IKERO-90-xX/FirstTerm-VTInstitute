package org.vtinstitute.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vtinstitute.utils.HttpResponse;

import java.io.IOException;

import org.vtinstitute.controller.LogsController;
import org.vtinstitute.models.ApiManager;
import org.vtinstitute.models.entity.Score;
import org.vtinstitute.models.entity.Student;
import org.vtinstitute.utils.RestApiConnection;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ScoreController {
    private ApiManager apiManager;
    
    public void qualifyStudent(int enrollmentId, int subjectId, int score) throws IOException {
        Map<String, Object> qualifyRequest = new HashMap();
        qualifyRequest.put("enrollment", enrollmentId);
        qualifyRequest.put("subject", subjectId);
        qualifyRequest.put("score", score);
        apiManager.qualifyApi(qualifyRequest);
    }
}
