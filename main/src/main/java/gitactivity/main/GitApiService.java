package gitactivity.main;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@Service
public class GitApiService {

    private final String baseApiLink = "https://gitlab.com/api/v4/projects/";
    private final OkHttpClient client = new OkHttpClient();

    Map<String, String> env = System.getenv();

    @Autowired
    GitApiRepository gitApiRepository;

    private ArrayList<Integer> getRepoIds(String rawApiData) {
        JSONObject group = new JSONObject(rawApiData);
//        System.out.println(group);

        ArrayList<Integer> repoIds = new ArrayList<>();

        JSONArray projects = new JSONArray(group.get("projects").toString());
        for (int i = 0; i < projects.length(); i++) {
            JSONObject currentRepo = (JSONObject) projects.get(i);

            Integer currentRepoId = (Integer) currentRepo.get("id");
            repoIds.add(currentRepoId);
        }

//        System.out.println(repoIds);
        return repoIds;
    }

    private ArrayList<String> getCommitsFromRepos(ArrayList<Integer> repoIds) {
        ArrayList<String> repoCommits = new ArrayList<>();

        for (Integer repoId : repoIds) {
            Request request = new Request.Builder()
                    .url(baseApiLink + repoId + "/repository/commits")
                    .addHeader("PRIVATE-TOKEN", env.get("GIT_API_KEY"))
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Запрос к серверу не был успешен: " +
                            response.code() + " " + response.message() + env.get("GIT_API_KEY"));
                }
                repoCommits.add(response.peekBody(Long.MAX_VALUE).string());
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        return repoCommits;
    }

    public ArrayList<String> getProcessedData() {
        String rawApiData = gitApiRepository.getGitData();

        ArrayList<Integer> repoIds = getRepoIds(rawApiData);

        ArrayList<String> allCommits = getCommitsFromRepos(repoIds);
//        System.out.println("[LOG] All commits: " + allCommits);

        return allCommits;
    }

}
