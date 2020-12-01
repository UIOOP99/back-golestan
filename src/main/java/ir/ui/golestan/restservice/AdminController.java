package ir.ui.golestan.restservice;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ir.ui.golestan.GolestanConfiguration;
import ir.ui.golestan.authorization.AuthorizationService;
import ir.ui.golestan.authorization.BaseController;

@RestController
public class AdminController extends BaseController {

    public AdminController(GolestanConfiguration configuration, AuthorizationService authorizationService) {
        super(configuration, authorizationService);
        // TODO Auto-generated constructor stub
    }
    
}
