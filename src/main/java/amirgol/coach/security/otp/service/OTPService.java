package amirgol.coach.security.otp.service;

public interface OTPService {
    String generateOtp(String email);

    boolean validateOtp(String email, String otp);
}

