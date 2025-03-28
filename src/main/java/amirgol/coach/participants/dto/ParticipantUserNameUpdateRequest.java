package amirgol.coach.participants.dto;

public record ParticipantUserNameUpdateRequest(
        String currentUserName,
        String updatedUserName) {
}