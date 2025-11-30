package org.vtinstitute.models;

public class Score {
    // Atributos
    private int code; // PK (INTEGER - scores_code_seq)
    private int enrollmentId; // FK
    private int subjectId; // FK
    private Integer score; // Usamos Integer para permitir valores NULL (calificación pendiente)

    // Constructor Vacío
    public Score(int code, int enrollmentId, int score) {
    }

    // Constructor Completo
    public Score(int code, int enrollmentId, int subjectId, Integer score) {
        this.code = code;
        this.enrollmentId = enrollmentId;
        this.subjectId = subjectId;
        this.score = score;
    }

    // Getters y Setters
    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public int getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(int enrollmentId) { this.enrollmentId = enrollmentId; }
    public int getSubjectId() { return subjectId; }
    public void setSubjectId(int subjectId) { this.subjectId = subjectId; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    @Override
    public String toString() {
        return "Score{" +
                "code=" + code +
                ", enrollmentId=" + enrollmentId +
                ", subjectId=" + subjectId +
                ", score=" + score +
                '}';
    }
}
