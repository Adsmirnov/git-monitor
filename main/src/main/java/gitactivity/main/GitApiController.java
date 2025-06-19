package gitactivity.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class GitApiController {

    @Autowired
    private GitApiService gitApiService;

    @GetMapping("/get/commits")
    public String getCommits() {
        String data = gitApiService.getProcessedData();
        return data;
    }
}
