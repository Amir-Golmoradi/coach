package amirgol.coach.participants.dto;

import jakarta.validation.constraints.Email;

public record ParticipantUpdateRequest(
        String username,

        @Email(message = "Email format is invalid")
        String email
) {
}
