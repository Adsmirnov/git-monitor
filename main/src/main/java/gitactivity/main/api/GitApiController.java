package gitactivity.main.api;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class GitApiController {  // Класс создан для тестов программы, в конечном результате будет убран

    @Autowired
    private GitApiService gitApiService;

    @Autowired
    private CommitParserService commitParserService;

    @GetMapping("/get/commits")
    public String getCommits() {
//        ArrayList<Pair<Integer, String>> data = gitApiService.getProcessedData();
        commitParserService.getParsedCommits();
        return "Success";
    }
}
