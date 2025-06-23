package gitactivity.main.javabot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;

import java.util.Map;

@Component
public class BotTaken implements SpringLongPollingBot {


    private final UpdateSoob updateSoob;

    public BotTaken(UpdateSoob updateSoob) {
        this.updateSoob = updateSoob;
    }

    private Map<String, String> env = System.getenv();

    @Override
    public String getBotToken() {
        return env.get("TG_BOT_TOKEN");
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {

        return updateSoob;
    }
}
