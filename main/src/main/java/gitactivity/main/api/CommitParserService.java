package gitactivity.main.api;

import gitactivity.main.model.Commit;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CommitParserService {

    private ArrayList<Commit> parsedCommits = new ArrayList<>();

    @Autowired
    GitApiService gitApiService;

    public ArrayList<Commit> getParsedCommits() {
        parsedCommits.clear();
        ArrayList<String> allCommits = gitApiService.getProcessedData();
        System.out.println("[COMMITS]");
        for (String allCommit : allCommits) {
            JSONArray currentRepoCommits = new JSONArray(allCommit);
            for (int j = 0; j < currentRepoCommits.length(); j++) {
                JSONObject currentCommit = new JSONObject(currentRepoCommits.get(j).toString());

                Commit commit = new Commit();
                commit.setId((String) currentCommit.get("id"));
                commit.setUser((String) currentCommit.get("committer_name"));
                commit.setComment((String) currentCommit.get("message"));
                parsedCommits.add(commit);

                System.out.println("[COMMIT] " + commit);
            }
        }

        return parsedCommits;
    }

}
