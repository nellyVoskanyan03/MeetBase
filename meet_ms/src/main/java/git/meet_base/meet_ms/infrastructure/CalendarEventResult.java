package git.meet_base.meet_ms.infrastructure;

public class CalendarEventResult {
    private final String eventId;
    private final String meetLink;

    public CalendarEventResult(String eventId, String meetLink) {
        this.eventId = eventId;
        this.meetLink = meetLink;
    }

    public String getEventId() { return eventId; }
    public String getMeetLink() { return meetLink; }
}