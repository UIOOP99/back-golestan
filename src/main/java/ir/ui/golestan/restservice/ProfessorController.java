package ir.ui.golestan.restservice;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ir.ui.golestan.data.entity.Score;
import ir.ui.golestan.data.repository.ScoreRepository;

@RestController
public class ProfessorController {
    private static final String template = "***User ID: %d, Score: %d, Course ID: %. 2f***";

	@GetMapping("/professor")
    public void addScore(@RequestParam(value = "studentId") int studentId,
                         @RequestParam(value = "courseId") int courseId,
                         @RequestParam(value = "score") double score,
                         ScoreRepository repository) {        // Repository???

        System.out.println(String.format(template, studentId, courseId, score));
        repository.save(new Score(studentId, courseId, score)); // @AllArgsConstructor?
        
	}
}
