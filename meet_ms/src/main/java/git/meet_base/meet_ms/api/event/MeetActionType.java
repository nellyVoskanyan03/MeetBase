package git.meet_base.meet_ms.api.event;

public enum MeetActionType {
    MEET_CREATED,          // Sent to Lecturer to accept/reject
    INVITATION_ACCEPTED,   // Sent to Company (Lecturer said yes)
    INVITATION_REJECTED,   // Sent to Company (Lecturer said no)
    STUDENT_REGISTERED,    // (Optional) Sent to Lecturer/Company just as an update
    APPROVAL_REQUIRED,     // Sent to Managers when the minimum student threshold is hit
    MEET_APPROVED,         // Sent to Everyone (Calendar links generated!)
    MEET_UPDATED,          // Sent to Everyone (Time/Location changed)
    MEET_CANCELLED         // Sent to Everyone (Meeting is off)
}