package gitactivity.main.api;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Map;

@Repository
public class GitApiRepository {

    private String groupLink = "";
    private final OkHttpClient client = new OkHttpClient();

    Map<String, String> env = System.getenv();

    public void setGroup(String link){
        this.groupLink = link;
    }

    public String getGitData() {  // Метод для создания запроса в GitLab API

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
