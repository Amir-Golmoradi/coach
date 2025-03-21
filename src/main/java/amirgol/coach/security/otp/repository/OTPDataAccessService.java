package amirgol.coach.security.otp.repository;

import amirgol.coach.security.otp.model.OTP;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OTPDataAccessService implements OtpDAO {
    private final OTPRepository otpRepository;

    @Transactional(readOnly = true)
    @Override
    public Optional<OTP> findByEmail(String email) {
        List<OTP> results = otpRepository.findByEmail(email);
        return results.stream().findFirst();
    }

    @Override
    public void save(OTP otp) {
       otpRepository.save(otp);
    }

    @Override
    public void delete(OTP otpEntity) {
        otpRepository.delete(otpEntity);
    }
}
