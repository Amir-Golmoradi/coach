package amirgol.coach.security;


import amirgol.coach.common.exception.CoachException;
import amirgol.coach.common.exception.Exceptions;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    /**
     * Sends an email containing OTP
     */
    public void sendOtpEmail(String to, String otp) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setTo(to);
            helper.setSubject("Your One-Time Password");
            helper.setText(
                    "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>" +
                            "<h2 style='color: #4285f4;'>One-Time Password</h2>" +
                            "<p>Your OTP code is: <strong style='font-size: 20px;'>" + otp + "</strong></p>" +
                            "<p>This code will expire in 5 minutes.</p>" +
                            "<p>If you did not request this code, please ignore this email.</p>" +
                            "</div>",
                    true
            );

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new CoachException(Exceptions.OTP_EMAIL_DELIVERY_FAILED);
        }
    }
}
