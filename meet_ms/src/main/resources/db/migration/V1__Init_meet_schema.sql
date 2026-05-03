CREATE TABLE meets
(
    id                       UUID PRIMARY KEY,
    course                   VARCHAR(255)             NOT NULL,
    company_id               UUID,
    lecturer_id              UUID,
    date_time                TIMESTAMP WITH TIME ZONE NOT NULL,
    place                    VARCHAR(255)             NOT NULL,
    min_student_count        INTEGER                  NOT NULL,
    actual_participants      INTEGER                  NOT NULL DEFAULT 0,
    status                   VARCHAR(50)              NOT NULL,
    google_calendar_event_id VARCHAR(255),
    hangout_link             VARCHAR(255),
    created_at               TIMESTAMP WITH TIME ZONE,
    updated_at               TIMESTAMP WITH TIME ZONE
);

CREATE TABLE meet_registrations
(
    id            UUID PRIMARY KEY,
    meet_id       UUID                     NOT NULL,
    student_id    UUID                     NOT NULL,
    registered_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_meet_registrations_meet_id
        FOREIGN KEY (meet_id)
            REFERENCES meets (id)
            ON DELETE CASCADE
);