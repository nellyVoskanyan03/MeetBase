package git.meet_base.meet_ms.infrastructure;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Collections;
import java.util.UUID;

@Component
public class GoogleCalendarClient {

    private final Calendar calendarService;
    private final String CALENDAR_ID = "voskanyannelli03@gmail.com";

    public GoogleCalendarClient() throws Exception {
        InputStream credentialsStream = new ClassPathResource("google-credentials.json").getInputStream();

        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream)
                .createScoped(Collections.singleton(CalendarScopes.CALENDAR));

        this.calendarService = new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))
                .setApplicationName("MeetBase-Microservice")
                .build();
    }

    public CalendarEventResult createMeetingWithHangoutLink(
            String summary,
            String description,
            String startTime,
            String endTime) throws Exception {

        Event event = new Event()
                .setSummary(summary)
                .setDescription(description);

        DateTime startDateTime = new DateTime(startTime);
        EventDateTime start = new EventDateTime().setDateTime(startDateTime);
        event.setStart(start);

        DateTime endDateTime = new DateTime(endTime);
        EventDateTime end = new EventDateTime().setDateTime(endDateTime);
        event.setEnd(end);

        ConferenceSolutionKey conferenceSlnKey = new ConferenceSolutionKey().setType("hangoutsMeet");
        CreateConferenceRequest createConferenceReq = new CreateConferenceRequest()
                .setRequestId(UUID.randomUUID().toString()) // Google requires a unique ID per request
                .setConferenceSolutionKey(conferenceSlnKey);
        ConferenceData conferenceData = new ConferenceData().setCreateRequest(createConferenceReq);
        event.setConferenceData(conferenceData);

        Event createdEvent = calendarService.events().insert(CALENDAR_ID, event)
                .execute();

        return new CalendarEventResult(createdEvent.getId(), createdEvent.getHangoutLink());
    }

    public void updateMeetingEvent(
            String eventId,
            String summary,
            String description,
            String startTime,
            String endTime) throws Exception {

        Event event = calendarService.events().get(CALENDAR_ID, eventId).execute();

        event.setSummary(summary);
        event.setDescription(description);

        DateTime start = new DateTime(startTime);
        event.setStart(new EventDateTime().setDateTime(start));

        DateTime end = new DateTime(endTime);
        event.setEnd(new EventDateTime().setDateTime(end));

        calendarService.events().patch(CALENDAR_ID, eventId, event)
                .execute();
    }

    public void deleteMeetingEvent(String eventId) throws Exception {
        if (eventId == null || eventId.isBlank()) {
            return;
        }
        calendarService.events().delete(CALENDAR_ID, eventId)
                .execute();
    }
}