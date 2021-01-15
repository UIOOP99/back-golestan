package ir.ui.golestan;

import ir.ui.golestan.data.entity.Course;
import ir.ui.golestan.data.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class GolestanApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(GolestanApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
