package git.meet_base.meet_ms.api.dto;

import jakarta.validation.constraints.NotBlank;

public class StudentRegisterRequest {

    @NotBlank(message = "Student ID is required")
    private String studentId;

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
}