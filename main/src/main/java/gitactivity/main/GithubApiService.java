package gitactivity.main;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GithubApiService {

    public String makeRequest() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.github.com/repos/adsmirnov/git-monitor/commits")
                .addHeader("Accept", "application/vnd.github+json")
                .addHeader("Authorization", "Bearer " + System.getProperty("GITHUB_API_KEY"))
                .addHeader("X-GitHub-Api-Version", "2022-11-28")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Запрос к серверу не был успешен: " +
                        response.code() + " " + response.message());
            }
//            System.out.println("Server: " + response.header("Server"));
//            System.out.println(response.body().string());
            return response.body().string();
        } catch (IOException e) {
//            System.out.println("Ошибка подключения: " + e);
            return "Ошибка подключения: " + e;
        }
    }

}
