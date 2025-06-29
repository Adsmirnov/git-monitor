package gitactivity.main.api;

import gitactivity.main.services.UserDailyStatService;
import gitactivity.main.services.UserHourlyStatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class GitApiController {  // Класс создан для тестов программы, в конечном результате будет убран


    private UserHourlyStatService userHourlyStatService;

    private UserDailyStatService userDailyStatService;

    public GitApiController(UserHourlyStatService userHourlyStatService, UserDailyStatService userDailyStatService) {
        this.userHourlyStatService = userHourlyStatService;
        this.userDailyStatService = userDailyStatService;
    }

    @GetMapping("/get/commits")
    public String getCommits() {
        userHourlyStatService.saveUserHourlyStat();
        userDailyStatService.saveUserDailyStat();
        return "Success";
    }
}
