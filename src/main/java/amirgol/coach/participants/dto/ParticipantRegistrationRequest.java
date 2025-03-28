package amirgol.coach.participants.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ParticipantRegistrationRequest(
        @NotNull(message = "Email cannot be null")
        @NotEmpty(message = "Email cannot be empty")
        @Email(message = "Email format is invalid")
        String email,

        @NotNull(message = "Username cannot be null")
        @NotEmpty(message = "Username cannot be empty")
        String username
) {
}


