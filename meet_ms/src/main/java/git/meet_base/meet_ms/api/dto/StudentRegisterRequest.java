package git.meet_base.meet_ms.api.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class StudentRegisterRequest {

    @NotNull(message = "Student ID is required")
    private UUID studentId;

    public UUID getStudentId() { return studentId; }
    public void setStudentId(UUID studentId) { this.studentId = studentId; }
}