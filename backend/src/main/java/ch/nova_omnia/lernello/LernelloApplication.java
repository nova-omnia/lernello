package ch.nova_omnia.lernello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LernelloApplication {
    private static Logger log = LoggerFactory.getLogger(LernelloApplication.class);

    public static void main(String[] args) {
        log.info("Starting PM4 application");
        SpringApplication.run(LernelloApplication.class, args);
    }

}
