package amirgol.coach.security.jwt;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Data
public class JwtProperties {
    @Value("${spring.security.jwt.secret}")
    private String secret;
    @Value("${spring.security.jwt.refresh}")
    private long refresh;
    @Value("${spring.security.jwt.expiration}")
    private long expiration;
}
