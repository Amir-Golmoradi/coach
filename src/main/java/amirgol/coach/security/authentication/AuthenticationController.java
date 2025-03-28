package amirgol.coach.security.authentication;

import amirgol.coach.security.EmailService;
import amirgol.coach.security.otp.dto.OTPGenerateRequest;
import amirgol.coach.security.otp.dto.OTPVerifyRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/otp")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;
    private final EmailService emailService; // Assuming you have an email service

    @PostMapping("/generate")
    public ResponseEntity<String> generateOtp(@Valid @RequestBody OTPGenerateRequest request) {
        var otp = authService.generateOtp(request.getEmail());

        // In production, send OTP via email
        emailService.sendOtpEmail(request.getEmail(), otp);

        return ResponseEntity.ok("OTP sent to your email");
    }

    @PostMapping("/verify")
    public ResponseEntity<AuthenticationResponse> verifyOtp(@Valid @RequestBody OTPVerifyRequest request) {
        AuthenticationResponse response = authService.verifyOtp(request.getEmail(), request.getOtp());
        return ResponseEntity.ok(response);
    }
}