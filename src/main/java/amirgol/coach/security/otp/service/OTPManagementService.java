package amirgol.coach.security.otp.service;

import amirgol.coach.security.otp.model.OTP;
import amirgol.coach.security.otp.repository.OtpDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

/**
 * Service for managing OTP (One-Time Password) generation and validation.
 * Implements {@link OTPService} to provide business logic for OTP operations.
 */
@Service
@RequiredArgsConstructor
public class OTPManagementService implements OTPService {

    private static final int OTP_VALIDITY_MINUTES = 5;
    private static final int OTP_LENGTH = 6;
    private static final int MAX_OTP_VALUE = 999999;

    private final OtpDAO otpDao; // Use the DAO abstraction instead of OTPRepository
    private final Random random = new Random();

    /**
     * Generates a new OTP for the given email and saves it to the database.
     *
     * @param email the email address to associate with the OTP
     * @return the generated OTP value
     */
    @Transactional
    @Override
    public String generateOtp(String email) {
        String otpValue = generateRandomOtp();

        OTP otp = new OTP();
        otp.setEmail(email);
        otp.setOtpCode(otpValue);
        otp.setExpiryTime(LocalDateTime.now().plusMinutes(OTP_VALIDITY_MINUTES));

        otpDao.save(otp);
        return otpValue;
    }

    /**
     * Validates an OTP for the given email and deletes it if valid.
     *
     * @param email the email address associated with the OTP
     * @param otp   the OTP value to validate
     * @return true if the OTP is valid and not expired, false otherwise
     */
    @Transactional
    @Override
    public boolean validateOtp(String email, String otp) {
        Optional<OTP> storedOtp = otpDao.findByEmail(email);

        if (storedOtp.isEmpty()) {
            return false;
        }

        OTP otpEntity = storedOtp.get();
        LocalDateTime now = LocalDateTime.now();

        if (otpEntity.getExpiryTime().isAfter(now) && otpEntity.getOtpCode().equals(otp)) {
            otpDao.delete(otpEntity); // Delete after successful validation
            return true;
        }
        return false;
    }
    private String generateRandomOtp() {
        return String.format("%0" + OTP_LENGTH + "d", random.nextInt(MAX_OTP_VALUE + 1));
    }
}
