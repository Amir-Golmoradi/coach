package amirgol.coach.security.otp.service;

import amirgol.coach.common.exception.CoachException;
import amirgol.coach.common.exception.Exceptions;
import amirgol.coach.participants.model.Participants;
import amirgol.coach.participants.repository.ParticipantDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OTPAuthenticationProvider implements AuthenticationProvider {

    private final OTPService otpService;
    private final ParticipantDAO participantDAO;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String participantEmail = authentication.getName();
        String otpPassword = authentication.getCredentials().toString();

        // Validate OTPs
        boolean isOtpValid = otpService.validateOtp(otpPassword, participantEmail);
        if (!isOtpValid) {
            log.error("OTP validation failed for user {}", participantEmail);
            throw new CoachException(Exceptions.OTP_INVALID);
        }

        // Find participants by email

        Optional<Participants> participantOpt = participantDAO.findByEmail(participantEmail);
        if (participantOpt.isEmpty()) {
            log.error("Participant with email {} not found", participantEmail);
            throw new CoachException(Exceptions.PARTICIPANT_NOT_FOUND);
        }
        Participants participant = participantOpt.get();

        // Mark email as verified after successful OTP validation
        participant.markEmailAsVerified();
        participant.resetFailedLoginAttempts();
        participant.updateLastLogin();
        participantDAO.save(participant);

        // Return authenticated token with authorities
        return new UsernamePasswordAuthenticationToken(
                participantEmail,
                null,
                participant.getAuthorities()
        );

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}