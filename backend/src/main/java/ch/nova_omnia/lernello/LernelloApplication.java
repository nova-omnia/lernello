package ch.nova_omnia.lernello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class LernelloApplication {
    private static final Logger log = LoggerFactory.getLogger(LernelloApplication.class);

    public static void main(String[] args) {
        log.info("Starting PM4 application");
        SpringApplication.run(LernelloApplication.class, args);
    }

}
