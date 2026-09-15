package lk.ijse.preordersystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PreOrderSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(PreOrderSystemApplication.class, args);
    }

}
