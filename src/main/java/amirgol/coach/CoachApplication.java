package amirgol.coach;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class CoachApplication {
    public static void main(String[] args) {
        log.info("CoachApplication started :D ");
        SpringApplication.run(CoachApplication.class, args);
    }
}
