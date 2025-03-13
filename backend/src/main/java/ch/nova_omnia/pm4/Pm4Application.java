package ch.nova_omnia.pm4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Pm4Application {
    private static Logger log = LoggerFactory.getLogger(Pm4Application.class);

    public static void main(String[] args) {
        log.info("Starting PM4 application");
        SpringApplication.run(Pm4Application.class, args);
    }

}
