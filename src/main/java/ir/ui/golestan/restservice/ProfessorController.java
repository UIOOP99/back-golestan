package ir.ui.golestan.restservice;

import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ir.ui.golestan.data.entity.Score;
import ir.ui.golestan.data.repository.ScoreRepository;
import ir.ui.golestan.GolestanConfiguration;
import ir.ui.golestan.authorization.AuthenticatedUser;
import ir.ui.golestan.authorization.AuthorizationService;
import ir.ui.golestan.authorization.BaseController;
import ir.ui.golestan.authorization.Role;

@RestController
public class ProfessorController extends BaseController {

    private final ScoreRepository scoreRepository;

    public ProfessorController(GolestanConfiguration configuration, AuthorizationService authorizationService,
            ScoreRepository scoreRepository) {
        super(configuration, authorizationService);
        this.scoreRepository = scoreRepository;
    }

    @GetMapping("/professor/set_score")
    public void setStudentScore(RequestEntity<?> request, int studentId, int courseId, 
                                double score) {
        
        AuthenticatedUser user = getAuthenticatedUser(request, Role.PROFESSOR);
        scoreRepository.save(Score.builder().studentId(studentId).courseId(courseId).score(score).build());
        
	}
}
