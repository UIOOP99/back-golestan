package ir.ui.golestan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ir.ui.golestan.data.repository.ScoreRepository;

@SpringBootApplication
public class GolestanApplication {

    public static void main(String[] args) {
        SpringApplication.run(GolestanApplication.class, args);
    }

}
