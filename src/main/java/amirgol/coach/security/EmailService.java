package amirgol.coach.security;

import amirgol.coach.common.exception.CoachException;
import amirgol.coach.common.exception.Exceptions;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String senderEmail;

    /**
     * Sends an email containing OTP.
     */
    public void sendOtpEmail(String to, String otp) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(senderEmail); // Ensure this matches your authenticated email
            helper.setTo(to);
            helper.setSubject("Your One-Time Password");
            helper.setText(
                    "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>" +
                            "<h2 style='color: #4285f4;'>One-Time Password</h2>" +
                            "<p>Your OTP code is: <strong style='font-size: 20px;'>" + otp + "</strong></p>" +
                            "<p>This code will expire in 5 minutes.</p>" +
                            "<p>If you did not request this code, please ignore this email.</p>" +
                            "</div>",
                    true // Enable HTML content
            );

            mailSender.send(mimeMessage);
            log.info("OTP email successfully sent to {}", to);

        } catch (MessagingException e) {
            log.error("Failed to send OTP email to {}: {}", to, e.getMessage());
            throw new CoachException(Exceptions.OTP_EMAIL_DELIVERY_FAILED);
        }
    }
}
