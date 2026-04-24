package git.meet_base.meet_ms.api.controller;

import git.meet_base.meet_ms.api.dto.*;
import git.meet_base.meet_ms.api.mapper.MeetMapper;
import git.meet_base.meet_ms.domain.model.MeetStatus;
import git.meet_base.meet_ms.domain.model.UpdateMeetCommand;
import git.meet_base.meet_ms.domain.model.UserRole;
import git.meet_base.meet_ms.domain.service.MeetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/meets")
public class MeetController {
    private final MeetService meetService;

    public MeetController(MeetService meetService) {
        this.meetService = meetService;
    }

    @PostMapping
    public ResponseEntity<MeetResponse> initializeMeeting(
            @Valid @RequestBody CreateMeetRequest createMeetDto) {

        MeetResponse createdMeet = MeetMapper.toDto(
                meetService.initializeMeeting(
                        MeetMapper.toDomain(createMeetDto)));

        return ResponseEntity.status(HttpStatus.CREATED).body(createdMeet);
    }

    @GetMapping
    public ResponseEntity<List<MeetResponse>> getMeets(
            @RequestParam() UserRole role,
            @RequestParam(required = false) MeetStatus status,
            @RequestParam(required = false) String companyId,
            @RequestParam() String userId) {


        List<MeetResponse> responseList = meetService
                .getFilteredMeets(role, status, companyId, userId)
                .stream()
                .map(MeetMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @PostMapping("/{id}/respond")
    public ResponseEntity<LecturerRespondResponse> respondToInvitation(
            @PathVariable("id") UUID meetId,
            @Valid @RequestBody LecturerRespondRequest request) {

        MeetResponse updatedMeet =
                MeetMapper.toDto(meetService.respondToInvitation(meetId, request.getLecturerId(), request.getAccepted()));


        LecturerRespondResponse response = new LecturerRespondResponse(
                "Lecturer response recorded.",
                updatedMeet.getMeetId(),
                updatedMeet.getStatus()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<StudentRegisterResponse> registerForMeet(
            @PathVariable("id") UUID meetId,
            @Valid @RequestBody StudentRegisterRequest request) {

        MeetResponse updatedMeet =
                MeetMapper.toDto(meetService.registerStudent(meetId, request.getStudentId()));

        StudentRegisterResponse response = new StudentRegisterResponse(
                "Successfully registered for the meeting.",
                updatedMeet.getMeetId(),
                updatedMeet.getActualParticipants(),
                updatedMeet.getStatus()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<ApproveMeetResponse> approveMeeting(@PathVariable("id") UUID meetId) {

        MeetResponse approvedMeet = MeetMapper.toDto(meetService.approveMeeting(meetId));

        ApproveMeetResponse response = new ApproveMeetResponse(
                "Meeting formally approved. Calendar event generated.",
                approvedMeet.getMeetId(),
                approvedMeet.getStatus(),
                approvedMeet.getGoogleCalendarEventId(),
                approvedMeet.getHangoutLink()
        );

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UpdateMeetResponse> updateMeeting(
            @PathVariable("id") UUID meetId,
            @RequestBody UpdateMeetRequest request) {

        MeetResponse updatedMeet =
                MeetMapper.toDto(meetService.updateMeeting(
                        meetId,
                        new UpdateMeetCommand(
                                request.getPlace(),
                                request.getDateTime()
                        )));

        Map<String, Object> updatedFields = new HashMap<>();
        if (request.getPlace() != null) {
            updatedFields.put("place", updatedMeet.getPlace());
        }
        if (request.getDateTime() != null) {
            updatedFields.put("dateTime", updatedMeet.getDateTime());
        }

        UpdateMeetResponse response = new UpdateMeetResponse(
                "Meeting updated successfully.",
                updatedMeet.getMeetId(),
                updatedFields,
                updatedMeet.getStatus()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CancelMeetResponse> cancelMeeting(@PathVariable("id") UUID meetId) {

        MeetResponse cancelledMeet =
                MeetMapper.toDto(meetService.cancelMeeting(meetId));

        CancelMeetResponse response = new CancelMeetResponse(
                "Meeting successfully cancelled.",
                cancelledMeet.getMeetId(),
                cancelledMeet.getStatus()
        );

        return ResponseEntity.ok(response);
    }
}
