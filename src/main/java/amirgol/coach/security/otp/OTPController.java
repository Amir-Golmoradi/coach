package amirgol.coach.security.otp;

import amirgol.coach.security.otp.dto.OTPGenerateRequest;
import amirgol.coach.security.otp.dto.OTPVerifyRequest;
import amirgol.coach.security.otp.service.OTPService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/otp")
@RequiredArgsConstructor
public class OTPController {
    private final OTPService otpService;

    @PostMapping
    public ResponseEntity<String> generateOtp(@Valid @RequestBody OTPGenerateRequest request) {
        String otp = otpService.generateOtp(request.getEmail());
        return new ResponseEntity<>(otp, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Boolean> verifyOtp(@Valid @RequestBody OTPVerifyRequest request) {
        boolean verify = otpService.validateOtp(request.getEmail(), request.getOtp());
        return new ResponseEntity<>(verify, HttpStatus.OK);
    }

}
