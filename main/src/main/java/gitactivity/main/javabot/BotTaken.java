package gitactivity.main.javabot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;

@Component
public class BotTaken implements SpringLongPollingBot {


    private final UpdateSoob updateSoob;

    public BotTaken(UpdateSoob updateSoob) {
        this.updateSoob = updateSoob;
    }

    @Autowired
    private Environment environment;

    @Override
    public String getBotToken() {
        return environment.getProperty("gitmonitor.bottoken");
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {

        return updateSoob;
    }
}
