package amirgol.coach.participants.repository;

import amirgol.coach.common.exception.CoachException;
import amirgol.coach.common.exception.Exceptions;
import amirgol.coach.participants.model.Participants;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ParticipantDataAccessService implements ParticipantDAO {
    private final ParticipantRepository repository;

    @Override
    @Transactional
    public Optional<List<Participants>> findAll() {
        return Optional.of(repository.findAll());
    }

    @Override
    @Transactional
    public Optional<Participants> findByEmail(String email) {
        if (!existsByEmail(email)) {
            throw new CoachException(Exceptions.PARTICIPANT_NOT_FOUND, "No Email found");
        }
        return Optional.of(repository.findByEmail(email));
    }

    @Override
    @Transactional
    public Optional<Participants> findByUsername(String username) {
        if (!existsByUsername(username)) {
            throw new CoachException(Exceptions.PARTICIPANT_NOT_FOUND, "No Username found");
        }
        return Optional.of(repository.findByUsername(username));    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    @Transactional
    public void save(Participants participant) {
        repository.save(participant);
    }

    @Override
    public void deleteByEmail(String email) {
        repository.deleteByEmail(email);
    }

    @Override
    public void deleteByUsername(String username) {
        repository.deleteByUsername(username);
    }
}
