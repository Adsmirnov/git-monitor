package gitactivity.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class GitApiController {

    @Autowired
    private GitApiService gitApiService;

    @Autowired
    private CommitParserService commitParserService;

    @GetMapping("/get/commits")
    public ArrayList<String> getCommits() {
        ArrayList<String> data = gitApiService.getProcessedData();
        commitParserService.getParsedCommits();
        return data;
    }
}
