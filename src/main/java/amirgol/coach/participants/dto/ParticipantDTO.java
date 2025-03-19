package amirgol.coach.participants.dto;

/**
 * Data Transfer Object (DTO) for a participant.
 * <p>
 * It contains only the most important information about a participant, i.e. the
 * email and the user name.
 *
 * @param email    the email address of the participant. This is the primary
 *                 identifier of a participant.
 * @param username the user name of the participant.
 */
public record ParticipantDTO(
        Long id,
        String email,
        String username
) {
}
