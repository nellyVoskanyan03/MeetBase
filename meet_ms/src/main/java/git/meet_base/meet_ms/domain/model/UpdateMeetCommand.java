package git.meet_base.meet_ms.domain.model;

import java.time.ZonedDateTime;

public class UpdateMeetCommand {
    private final String place;
    private final ZonedDateTime dateTime;

    public UpdateMeetCommand(String place, ZonedDateTime dateTime) {
        this.place = place;
        this.dateTime = dateTime;
    }

    public String getPlace() { return place; }
    public ZonedDateTime getDateTime() { return dateTime; }
}