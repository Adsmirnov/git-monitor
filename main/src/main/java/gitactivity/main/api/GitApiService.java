package gitactivity.main.api;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.javatuples.Pair;
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

        ArrayList<Integer> repoIds = new ArrayList<>();

        JSONArray projects = new JSONArray(group.get("projects").toString());

        System.out.println("[REPOS]");
        for (int i = 0; i < projects.length(); i++) {
            JSONObject currentRepo = (JSONObject) projects.get(i);

            Integer currentRepoId = (Integer) currentRepo.get("id");

            repoIds.add(currentRepoId);
            System.out.println("[REPO] " + currentRepoId);
        }

        return repoIds;
    }

    private ArrayList<Pair<Integer, String>> getCommitsFromRepos(ArrayList<Integer> repoIds, LocalDateTime since, LocalDateTime until) {  // Метод для получения всех коммитов группы
        ArrayList<Pair<Integer, String>> repoCommits = new ArrayList<>();

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
                repoCommits.add(new Pair<Integer, String>(repoId, response.peekBody(Long.MAX_VALUE).string()));
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        return repoCommits;
    }

    public Integer getChangedLines(Integer repoId, String id) {
        HttpUrl repoUrl = new HttpUrl.Builder()
                .scheme("https")
                .host("gitlab.com")
                .addPathSegment("api")
                .addPathSegment("v4")
                .addPathSegment("projects")
                .addPathSegment(repoId.toString())
                .addPathSegment("repository")
                .addPathSegment("commits")
                .addPathSegment(id)
                .addQueryParameter("stats", "true")
                .build();

        Request request = new Request.Builder()
                .url(repoUrl)
                .addHeader("PRIVATE-TOKEN", env.get("GIT_API_KEY"))
                .build();

        Integer changedLines = 0;
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Запрос к серверу не был успешен: " +
                        response.code() + " " + response.message() + env.get("GIT_API_KEY"));
            }
            JSONObject fullStats = new JSONObject(response.peekBody(Long.MAX_VALUE).string());
            JSONObject stats = (JSONObject) fullStats.get("stats");
            changedLines = (Integer) stats.get("total");
        } catch (IOException e) {
            System.out.println(e);
        }
        return changedLines;
    }

    public ArrayList<Pair<Integer, String>> getProcessedData(LocalDateTime since, LocalDateTime until) {  // Общий публичный метод для получения всех коммитов группы
        String rawApiData = gitApiRepository.getGitData();

        ArrayList<Integer> repoIds = getRepoIds(rawApiData);

        ArrayList<Pair<Integer, String>> allCommits = getCommitsFromRepos(repoIds, since.minusHours(3), until.minusHours(3));

        return allCommits;
    }

    public void setGroupLink(String link) {
        gitApiRepository.setGroup(link);
    }

}
