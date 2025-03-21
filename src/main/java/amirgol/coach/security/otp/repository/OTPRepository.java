package amirgol.coach.security.otp.repository;

import amirgol.coach.security.otp.model.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Long> {
    List<OTP> findByEmail(String email);
}
