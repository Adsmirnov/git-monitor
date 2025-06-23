package gitactivity.main.api;

import gitactivity.main.model.Commit;
import org.javatuples.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
public class CommitParserService {

    private ArrayList<Commit> parsedCommits = new ArrayList<>();

    @Autowired
    GitApiService gitApiService;

    public ArrayList<Commit> getParsedCommits() {
        parsedCommits.clear();
        ArrayList<Pair<Integer, String>> allCommits = gitApiService.getProcessedData();

        System.out.println("[COMMITS]");
        for (int i = 0; i < allCommits.size(); i++) {
            Integer repoId = allCommits.get(i).getValue0();
            String allCommit = allCommits.get(i).getValue1();

            JSONArray currentRepoCommits = new JSONArray(allCommit);
            for (int j = 0; j < currentRepoCommits.length(); j++) {
                JSONObject currentCommit = new JSONObject(currentRepoCommits.get(j).toString());

                Commit commit = new Commit();

                commit.setId((String) currentCommit.get("id"));
                commit.setRepoId(repoId);
                commit.setUser((String) currentCommit.get("committer_name"));
                commit.setComment((String) currentCommit.get("message"));

                // работа с датой
                String dateTime = (String) currentCommit.get("committed_date");
                dateTime = dateTime.substring(0, 19);
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                LocalDateTime parsedDate = LocalDateTime.parse(dateTime, formatter);
                LocalDateTime parsedDateLocal = parsedDate.plusHours(3);
                commit.setDate(parsedDateLocal);

                parsedCommits.add(commit);
                System.out.println("[COMMIT] " + commit);
            }
        }

        return parsedCommits;
    }

}
