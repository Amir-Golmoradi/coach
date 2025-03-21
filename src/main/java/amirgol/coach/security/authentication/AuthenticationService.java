package amirgol.coach.security.authentication;


import amirgol.coach.participants.model.Participants;
import amirgol.coach.participants.model.role.Role;
import amirgol.coach.participants.repository.ParticipantDAO;
import amirgol.coach.security.jwt.JwtService;
import amirgol.coach.security.otp.service.OTPService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final OTPService otpService;
    private final JwtService jwtService;
    private final ParticipantDAO participantDAO;
    private final AuthenticationManager authenticationManager;

    /**
     * Generates OTP for user login
     */
    @Transactional
    public String generateOtp(String email) {
        Optional<Participants> participantOpt = participantDAO.findByEmail(email);
        Participants participant;

        if (participantOpt.isEmpty()) {
            participant = Participants.builder()
                    .email(email)
                    .username(email.split("@")[0])
                    .roles(Set.of(Role.MEMBER))
                    .build();
            participantDAO.save(participant);
        } else {
            participant = participantOpt.get();
        }

        return otpService.generateOtp(email);
    }

    /**
     * Verifies OTP and returns JWT tokens if valid
     */
    public AuthenticationResponse verifyOtp(String email, String otp) {
        // Authenticate with OTP
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, otp)
        );

        // Load user details
        Participants participant = participantDAO.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate tokens
        String accessToken = jwtService.generateToken(participant);
        String refreshToken = jwtService.generateRefreshToken(participant);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
