package amirgol.coach.participants.dto;

public record ParticipantEmailUpdateRequest(
        String currentEmail,
        String newEmail) {
}
