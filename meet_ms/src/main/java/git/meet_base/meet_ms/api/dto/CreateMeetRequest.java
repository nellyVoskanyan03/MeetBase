package git.meet_base.meet_ms.api.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;

public class CreateMeetRequest {

    @NotBlank(message = "Course name is required")
    private String course;

    private String companyId;
    private String lecturerId;

    @NotNull(message = "Date and time are required")
    @Future(message = "Meeting date and time must be in the future")
    private ZonedDateTime dateTime;

    @NotBlank(message = "Place is required")
    private String place;

    @Min(value = 1, message = "Minimum student count must be at least 1")
    private int minStudentCount;

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }

    public String getLecturerId() { return lecturerId; }
    public void setLecturerId(String lecturerId) { this.lecturerId = lecturerId; }

    public ZonedDateTime getDateTime() { return dateTime; }
    public void setDateTime(ZonedDateTime dateTime) { this.dateTime = dateTime; }

    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }

    public int getMinStudentCount() { return minStudentCount; }
    public void setMinStudentCount(int minStudentCount) { this.minStudentCount = minStudentCount; }
}