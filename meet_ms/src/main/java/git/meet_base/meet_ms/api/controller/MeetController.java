package git.meet_base.meet_ms.api.controller;

import git.meet_base.meet_ms.api.mapper.MeetMapper;
import git.meet_base.meet_ms.api.dto.CreateMeetRequest;
import git.meet_base.meet_ms.api.dto.CreateMeetResponse;
import git.meet_base.meet_ms.domain.service.MeetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meets")
public class MeetController {
    private final MeetService meetService;

    public MeetController(MeetService meetService) {
        this.meetService = meetService;
    }

    @PostMapping
    public ResponseEntity<CreateMeetResponse> initializeMeeting(
            @Valid @RequestBody CreateMeetRequest createMeetDto) {

        CreateMeetResponse createdMeet = MeetMapper.toDto(
                meetService.initializeMeeting(
                        MeetMapper.toDomain(createMeetDto)));

        return ResponseEntity.status(HttpStatus.CREATED).body(createdMeet);
    }
}
