package ir.ui.golestan.restservice;


import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ir.ui.golestan.data.entity.Score;
import ir.ui.golestan.data.repository.ScoreRepository;
import ir.ui.golestan.authorization.AuthenticatedUser;
import ir.ui.golestan.authorization.Role;

@RestController
public class ProfessorController {

	@GetMapping("/professor")
    public void setStudentScore(RequestEntity<?> request, int studentId, int courseId, 
                                double score, ScoreRepository repository) {// Repository???
        
        AuthenticatedUser user = getAuthenticatedUser(request, Role.PROFESSOR);
        repository.save(new Score(studentId, courseId, score)); // @AllArgsConstructor?
        
	}
}
