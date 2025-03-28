package amirgol.coach.participants.controller;

import amirgol.coach.participants.dto.ParticipantDTO;
import amirgol.coach.participants.dto.ParticipantRegistrationRequest;
import amirgol.coach.participants.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/participants")
@RequiredArgsConstructor
public class ParticipantController {
    private final ParticipantService participantService;

    @GetMapping()
    public ResponseEntity<Optional<List<ParticipantDTO>>> findAll() {
        return ResponseEntity.ok(participantService.findAll());
    }

    @GetMapping("/{email}")
    public ResponseEntity<Optional<ParticipantDTO>> findByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(participantService.findByEmail(email));
    }

    @GetMapping("/{username}")
    public ResponseEntity<Optional<ParticipantDTO>> findByUsername(@PathVariable("username") String username) {
        return ResponseEntity.ok(participantService.findByUsername(username));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody ParticipantRegistrationRequest registrationRequest) {
        participantService.register(registrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
