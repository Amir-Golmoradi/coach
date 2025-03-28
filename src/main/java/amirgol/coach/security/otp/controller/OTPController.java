package amirgol.coach.security.otp.controller;

import amirgol.coach.security.otp.dto.OTPGenerateRequest;
import amirgol.coach.security.otp.dto.OTPVerifyRequest;
import amirgol.coach.security.otp.service.OTPService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("api/otp")
@RequiredArgsConstructor
public class OTPController {
    private final OTPService otpService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateOtp(@Valid @RequestBody OTPGenerateRequest request) {
        try {
            String otp = otpService.generateOtp(request.getEmail());
            return ResponseEntity.ok(otp);
        } catch (Exception ex) {
            OTPController.log.error("Failed to generate OTP: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to generate OTP");
        }
    }



    @PostMapping("/verify")
    public ResponseEntity<Boolean> verifyOtp(@Valid @RequestBody OTPVerifyRequest request) {
        boolean verify = otpService.validateOtp(request.getEmail(), request.getOtp());
        return new ResponseEntity<>(verify, HttpStatus.OK);
    }

}
