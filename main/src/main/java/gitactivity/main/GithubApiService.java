package gitactivity.main;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GithubApiService {

    @Autowired
    GithubApiRepository githubApiRepository;

    public String getProcessedData() {
        String rawApiData = githubApiRepository.getGithubData();
        JSONArray jsonArray = new JSONArray(rawApiData);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject currentCommit = (JSONObject) jsonArray.get(i);
            System.out.println(currentCommit);
        }

        return rawApiData;
    }

}
