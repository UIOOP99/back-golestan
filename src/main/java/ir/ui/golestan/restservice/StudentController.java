package ir.ui.golestan.restservice;

import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ir.ui.golestan.GolestanConfiguration;
import ir.ui.golestan.authorization.AuthenticatedUser;
import ir.ui.golestan.authorization.AuthorizationService;
import ir.ui.golestan.authorization.BaseController;
import ir.ui.golestan.authorization.Role;
import ir.ui.golestan.data.repository.ScoreRepository;

@RestController
public class StudentController extends BaseController {

    private final ScoreRepository scoreRepository;

    public StudentController(GolestanConfiguration configuration, AuthorizationService authorizationService,
            ScoreRepository scoreRepository) {
        super(configuration, authorizationService);
        this.scoreRepository = scoreRepository;
    }



    @GetMapping("/student/get_scores")
    public void getStudentScores(RequestEntity<?> request) {
        
        AuthenticatedUser user = getAuthenticatedUser(request, Role.STUDENT);
        
        return scoreRepository.findScoresBystudentId(user.userId);
        
        
	}
    
}
