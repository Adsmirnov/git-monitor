package com.example.javabot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;

@Component
public class BotTaken implements SpringLongPollingBot {


    private final UpdateSoob updateSoob;

    public BotTaken(UpdateSoob updateSoob) {
        this.updateSoob = updateSoob;
    }


    @Override
    public String getBotToken() {
        return "7782840064:AAHCj2E3t1fy-drqh-d72iYu__EBTI2B8Gc";
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {

        return updateSoob;
    }
}
