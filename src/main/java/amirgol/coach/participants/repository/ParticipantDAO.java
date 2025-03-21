package amirgol.coach.participants.repository;

import amirgol.coach.participants.model.Participants;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantDAO {
    Optional<List<Participants>> findAll();

    Optional<Participants> findByEmail(String email);

    Optional<Participants> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    void deleteByEmail(String email);

    void deleteByUsername(String username);

    void save(Participants participants);
}
