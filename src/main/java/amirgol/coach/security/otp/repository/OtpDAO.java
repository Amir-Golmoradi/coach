package amirgol.coach.security.otp.repository;


import amirgol.coach.security.otp.model.OTP;

import java.util.Optional;


public interface OtpDAO {
    Optional<OTP> findByEmail(String email);

    void save(OTP otp);

    void delete(OTP otpEntity);

}


