package amirgol.coach.participants.repository;

import amirgol.coach.participants.model.Participants;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participants, Long> {
    Participants findByEmail(String email);

    Participants findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    void deleteByEmail(String email);

    void deleteByUsername(String username);
}
