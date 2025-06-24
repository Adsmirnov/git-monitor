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
import java.util.HashMap;
import java.util.Map;

@Service
public class CommitParserService {

    private HashMap<String, ArrayList<Commit>> parsedCommits = new HashMap<>();

    @Autowired
    GitApiService gitApiService;

    public Map<String, ArrayList<Commit>> getParsedCommits(LocalDateTime since, LocalDateTime until) {
        parsedCommits.clear();
        ArrayList<Pair<Integer, String>> allCommits = gitApiService.getProcessedData(since, until);

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

                // количество изменённых линий
                commit.setChangedLines(gitApiService.getChangedLines(repoId, (String) currentCommit.get("id")));

                // работа с датой
                String dateTime = (String) currentCommit.get("committed_date");
                dateTime = dateTime.substring(0, 19);
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                LocalDateTime parsedDate = LocalDateTime.parse(dateTime, formatter);
                LocalDateTime parsedDateLocal = parsedDate.plusHours(3);
                commit.setDate(parsedDateLocal);

                try {
                    ArrayList<Commit> commits = parsedCommits.get(commit.getUser());
                    commits.add(commit);
                    parsedCommits.put(commit.getUser(), commits);
                } catch (Exception e) {
                    ArrayList<Commit> commits = new ArrayList<>();
                    commits.add(commit);
                    parsedCommits.put(commit.getUser(), commits);
                }
            }
        }

        return parsedCommits;
    }

}
