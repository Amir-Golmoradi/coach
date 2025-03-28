package amirgol.coach.participants.service;

import java.util.List;
import java.util.Optional;

import amirgol.coach.participants.dto.ParticipantDTO;
import amirgol.coach.participants.dto.ParticipantRegistrationRequest;
import amirgol.coach.participants.dto.ParticipantUpdateRequest;
import org.springframework.stereotype.Service;

@Service
public interface ParticipantService {
  Optional<List<ParticipantDTO>> findAll();
  Optional<ParticipantDTO> findByEmail(String email);
  Optional<ParticipantDTO> findByUsername(String username);
  void register(ParticipantRegistrationRequest registrationRequest);
  void update(ParticipantUpdateRequest updateRequest, String email);
  boolean existsByEmail(String email);
  boolean existsByUsername(String username);
  void deleteByEmail(String email);
  void deleteByUsername(String username);
}
