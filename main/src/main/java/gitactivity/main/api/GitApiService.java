package gitactivity.main.api;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

@Service
public class GitApiService {

    private final OkHttpClient client = new OkHttpClient();

    Map<String, String> env = System.getenv();

    @Autowired
    GitApiRepository gitApiRepository;

    private ArrayList<Integer> getRepoIds(String rawApiData) {  // Метод для получения списка айди всех репозиториев данной группы
        JSONObject group = new JSONObject(rawApiData);
//        System.out.println(group);

        ArrayList<Integer> repoIds = new ArrayList<>();

        JSONArray projects = new JSONArray(group.get("projects").toString());

        System.out.println("[REPOS]");
        for (int i = 0; i < projects.length(); i++) {
            JSONObject currentRepo = (JSONObject) projects.get(i);

            Integer currentRepoId = (Integer) currentRepo.get("id");

            repoIds.add(currentRepoId);
            System.out.println("[REPO] " + currentRepoId);
        }

//        System.out.println(repoIds);
        return repoIds;
    }

    private ArrayList<String> getCommitsFromRepos(ArrayList<Integer> repoIds, LocalDateTime since, LocalDateTime until) {  // Метод для получения всех коммитов группы
        ArrayList<String> repoCommits = new ArrayList<>();

        for (Integer repoId : repoIds) {

            // построение ссылки на репозиторий
            HttpUrl repoUrl = new HttpUrl.Builder()
                    .scheme("https")
                    .host("gitlab.com")
                    .addPathSegment("api")
                    .addPathSegment("v4")
                    .addPathSegment("projects")
                    .addPathSegment(repoId.toString())
                    .addPathSegment("repository")
                    .addPathSegment("commits")
                    .addQueryParameter("since", since.toString())
                    .addQueryParameter("until", until.toString())
                    .addQueryParameter("all", "true")
                    .build();

            Request request = new Request.Builder()
                    .url(repoUrl)
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

    public ArrayList<String> getProcessedData() {  // Общий публичный метод для получения всех коммитов группы
        String rawApiData = gitApiRepository.getGitData();

        ArrayList<Integer> repoIds = getRepoIds(rawApiData);

        // для тестирования получения данных за временной промежуток
        // время указывается в МСК (UTC+3)
        // minusHours(3) - поправка на UTC
        LocalDateTime since = LocalDateTime.parse("2024-06-20T10:00:00").minusHours(3);
        LocalDateTime until = LocalDateTime.parse("2025-06-22T12:16:00").minusHours(3);

        ArrayList<String> allCommits = getCommitsFromRepos(repoIds, since, until);
//        System.out.println("[LOG] All commits: " + allCommits);

        return allCommits;
    }

}
