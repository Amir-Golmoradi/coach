package amirgol.coach.participants.service.usecase;

import amirgol.coach.common.exception.CoachException;
import amirgol.coach.common.exception.Exceptions;
import amirgol.coach.participants.dto.ParticipantDTO;
import amirgol.coach.participants.dto.ParticipantEmailUpdateRequest;
import amirgol.coach.participants.dto.ParticipantRegistrationRequest;
import amirgol.coach.participants.dto.ParticipantUserNameUpdateRequest;
import amirgol.coach.participants.dto.mapper.ParticipantMapper;

import amirgol.coach.participants.model.Participants;
import amirgol.coach.participants.model.vo.UserName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParticipantManagementService implements ParticipantService {
    private final ParticipantDAO participantDAO;
    private final ParticipantMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<List<ParticipantDTO>> findAll() {
        // Fetch all participants from the DAO layer
        List<Participants> participants = participantDAO.findAll()
                .orElseThrow(() -> new CoachException(Exceptions.PARTICIPANT_NOT_FOUND, "No participants found."));

        // Map the list of entities to DTOs
        List<ParticipantDTO> participantDTOs = participants.stream()
                .map(mapper::mapToParticipantDTO)
                .toList();

        // Return the result wrapped in an Optional
        return Optional.of(participantDTOs);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ParticipantDTO> findByEmail(Email email) {
        if (!participantDAO.existsByEmail(email)) {
            throw new CoachException(Exceptions.PARTICIPANT_NOT_FOUND, "Participant with email" + email.getValue() + "not found");
        }
        return participantDAO.findByEmail(email)
                .stream()
                .map(mapper::mapToParticipantDTO)
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ParticipantDTO> findByUserName(UserName username) {

        if (!participantDAO.existsByUserName(username)) {
            throw new CoachException(Exceptions.PARTICIPANT_NOT_FOUND, "Participant with username" + username.getValue() + " not found.");
        }
        return participantDAO
                .findByUserName(username)
                .stream()
                .map(mapper::mapToParticipantDTO)
                .findFirst();
    }

    @Override
    public void register(ParticipantRegistrationRequest registrationRequest) {
        Participants participant = Participants
                .builder()
                .id(registrationRequest.id())
                .username(registrationRequest.userName())
                .email(registrationRequest.email())
                .build();
        participantDAO.save(participant);

    }

    @Override
    public ParticipantDTO updateParticipantEmail(ParticipantEmailUpdateRequest updateRequest, Email email) {

        // Try to retrieve the participant by the given email
        Optional<Participants> participantOpt = participantDAO.findByEmail(email);
        if (participantOpt.isEmpty()) {
            throw new CoachException(Exceptions.PARTICIPANT_NOT_FOUND, participantOpt + " not found");
        }
        Participants participant = participantOpt.get();
        // Validate that the current participant's email that is stored, matches the request
        String participantCurrentEmail = participant.getEmail();
        if (!participantCurrentEmail.equals(updateRequest.currentEmail())) {
            throw new CoachException(Exceptions.INVALID_EMAIL_FORMAT, "Provided email does not match stored email.");

        }
        // Build the new email value
        var newEmail = Email.of(updateRequest.newEmail());

        // Call the domain method to update the email; the domain logic will also trigger its own event if needed.
        Participants result = participant.updateEmail(newEmail);
        if (result == null) {
            throw new CoachException(Exceptions.INVALID_EMAIL_UPDATE, "Failed to update email " + newEmail);
        }
        // Persist the updated participant
        participantDAO.save(participant);

        // Map the updated participant to a DTO and return
        return mapper.mapToParticipantDTO(participant);
    }

    @Override
    public ParticipantDTO updateParticipantUserName(ParticipantUserNameUpdateRequest userNameUpdateRequest, UserName username) {
        // Try to retrieve the participant by the given username
        Optional<Participants> participantOpt = participantDAO.findByUserName(username);
        if (participantOpt.isEmpty()) {
            throw new CoachException(Exceptions.PARTICIPANT_NOT_FOUND, participantOpt + " not found");
        }
        Participants participant = participantOpt.get();
        // Validate that the current username stored in the participant matches the request
        // If the participant's current username does not match the requested username, reject the change request.
        String participantCurrentName = participant.getUsername();
        if (!participantCurrentName.equals(userNameUpdateRequest.currentUserName())) {
            throw new CoachException(Exceptions.INVALID_NAME_FORMAT, participantCurrentName + " username does not match the participant's stored username.");
        }
        // Build new UserName value.
        UserName name = UserName.of(userNameUpdateRequest.updatedUserName());

        // Call the domain method to update the username; the domain logic will also trigger its own event if needed.
        Participants result = participant.updateUsername(name);
        if (result == null) {
            throw new CoachException(Exceptions.INVALID_NAME_FORMAT, participantCurrentName + " username does not match the participant's stored username.");

        }
        participantDAO.save(participant);

        return mapper.mapToParticipantDTO(participant);

    }

    @Override
    public void deleteByEmail(Email deletedEmail) {
        participantDAO.deleteByEmail(deletedEmail);
    }

    @Override
    public void deleteByUserName(UserName deletedUserName) {

        participantDAO.deleteByUserName(deletedUserName);

    }

}
