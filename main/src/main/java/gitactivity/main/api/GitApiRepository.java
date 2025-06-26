package gitactivity.main.api;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Objects;

@Repository
public class GitApiRepository {

    private String groupLink = "";
    private final OkHttpClient client = new OkHttpClient();

    private final Environment environment;

    public GitApiRepository(Environment environment) {
        this.environment = environment;
    }

    public void setGroup(String link) {
        this.groupLink = link;
    }

    public String getGitData() {  // Метод для создания запроса в GitLab API

        Request request = new Request.Builder()
                .url(groupLink)
                .addHeader("PRIVATE-TOKEN", Objects.requireNonNull(environment.getProperty("gitmonitor.gitapikey")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Запрос к серверу не был успешен: " +
                        response.code() + " " + response.message() + environment.getProperty("gitmonitor.gitapikey"));
            }
            return response.peekBody(Long.MAX_VALUE).string();
        } catch (IOException e) {
            return "Ошибка подключения: " + e;
        }
    }
}
