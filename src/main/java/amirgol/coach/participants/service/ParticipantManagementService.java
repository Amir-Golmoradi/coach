package amirgol.coach.participants.service;

import amirgol.coach.common.exception.CoachException;
import amirgol.coach.common.exception.Exceptions;
import amirgol.coach.participants.dto.ParticipantDTO;
import amirgol.coach.participants.dto.ParticipantMapper;
import amirgol.coach.participants.dto.ParticipantRegistrationRequest;
import amirgol.coach.participants.dto.ParticipantUpdateRequest;
import amirgol.coach.participants.model.Participants;
import amirgol.coach.participants.repository.ParticipantDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipantManagementService implements ParticipantService {
    private final ParticipantDAO participantDAO;
    private final ParticipantMapper mapper;

    @Override
    public Optional<List<ParticipantDTO>> findAll() {
        return participantDAO.findAll()
                .map(list -> list
                        .stream()
                        .map(mapper::mapToParticipantDTO)
                        .toList()
                );
    }

    @Override
    public Optional<ParticipantDTO> findByEmail(String email) {
        return participantDAO.findByEmail(email)
                .stream()
                .map(mapper::mapToParticipantDTO)
                .findFirst();
    }

    @Override
    public Optional<ParticipantDTO> findByUsername(String username) {
        return participantDAO.findByUsername(username)
                .stream()
                .map(mapper::mapToParticipantDTO)
                .findFirst();
    }

    @Override
    public void register(ParticipantRegistrationRequest registrationRequest) {
        var registeredParticipant = Participants
                .builder()
                .email(registrationRequest.email())
                .username(registrationRequest.username())
                .build();
        participantDAO.save(registeredParticipant);
    }

    @Override
    public void update(ParticipantUpdateRequest updateRequest, String email) {
        // 1. is there anything to update at all ?
        var participant =
                participantDAO.findByEmail(email)
                        .orElseThrow(() ->
                                new CoachException(Exceptions.PARTICIPANT_NOT_FOUND, email));

        boolean isModified = false;

        if (updateRequest.username() != null && !updateRequest.username().equals(participant.getUsername())) {
            participant.updateUsername(updateRequest.username());
            isModified = true;
        }

        if (updateRequest.email() != null && !updateRequest.email().equals(email)) {
            participant.updateEmail(updateRequest.email());
            participant.setEmailVerified(false); // Require re-verification
            isModified = true;
        }

        if (isModified) {
            participantDAO.save(participant);
        }

        participantDAO.save(participant);
    }

    @Override
    public boolean existsByEmail(String email) {
        log.info("Checking if email {} exists", email);
        return participantDAO.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        log.info("Checking if username {} exists", username);
        return participantDAO.existsByUsername(username);
    }

    @Override
    public void deleteByEmail(String email) {
        log.info("Deleting participant with email {}", email);
        participantDAO.deleteByEmail(email);
    }

    @Override
    public void deleteByUsername(String username) {
        log.info("Deleting participant by username: {}", username);
        participantDAO.deleteByUsername(username);
    }

}
