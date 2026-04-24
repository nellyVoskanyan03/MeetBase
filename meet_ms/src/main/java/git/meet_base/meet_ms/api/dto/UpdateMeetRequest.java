package git.meet_base.meet_ms.api.dto;

import jakarta.validation.constraints.Future;

import java.time.ZonedDateTime;

public class UpdateMeetRequest {

    private String place;

    @Future(message = "Updated meeting date and time must be in the future")
    private ZonedDateTime dateTime;

    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }

    public ZonedDateTime getDateTime() { return dateTime; }
    public void setDateTime(ZonedDateTime dateTime) { this.dateTime = dateTime; }
}