package gitactivity.main.api;

import gitactivity.main.services.UserDailyStatService;
import gitactivity.main.services.UserHourlyStatService;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class GitApiController {  // Класс создан для тестов программы, в конечном результате будет убран

    @Autowired
    private UserHourlyStatService userHourlyStatService;

    @Autowired
    private UserDailyStatService userDailyStatService;

    @GetMapping("/get/commits")
    public String getCommits() {
        userHourlyStatService.saveUserHourlyStat();
        userDailyStatService.saveUserDailyStat();
        return "Success";
    }
}
