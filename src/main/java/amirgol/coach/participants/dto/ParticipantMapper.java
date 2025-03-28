package amirgol.coach.participants.dto;


import amirgol.coach.participants.model.Participants;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {

    ParticipantDTO mapToParticipantDTO(Participants participant);

}






