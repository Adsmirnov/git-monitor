package gitactivity.main.api;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Map;

@Repository
public class GitApiRepository {

    private final String groupLink = "https://gitlab.com/api/v4/groups/109642057";
    private final OkHttpClient client = new OkHttpClient();

    Map<String, String> env = System.getenv();

    public String getGitData() {

        Request request = new Request.Builder()
                .url(groupLink)
                .addHeader("PRIVATE-TOKEN", env.get("GIT_API_KEY"))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Запрос к серверу не был успешен: " +
                        response.code() + " " + response.message() + env.get("GIT_API_KEY"));
            }
            return response.peekBody(Long.MAX_VALUE).string();
        } catch (IOException e) {
            return "Ошибка подключения: " + e;
        }
    }
}
