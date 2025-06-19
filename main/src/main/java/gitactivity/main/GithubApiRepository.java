package gitactivity.main;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Map;

@Repository
public class GithubApiRepository {

    Map<String, String> env = System.getenv();

    public String getGithubData() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.github.com/repos/adsmirnov/git-monitor/commits")
                .addHeader("Accept", "application/vnd.github+json")
                .addHeader("Authorization", "Bearer " + env.get("GITHUB_API_KEY"))
                .addHeader("X-GitHub-Api-Version", "2022-11-28")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Запрос к серверу не был успешен: " +
                        response.code() + " " + response.message() + env.get("GITHUB_API_KEY"));
            }
            return response.body().string();
        } catch (IOException e) {
            return "Ошибка подключения: " + e;
        }
    }
}
