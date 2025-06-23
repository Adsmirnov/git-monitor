package com.example.javabot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

@Component
public class UpdateSoob implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;

    public UpdateSoob() {
        this.telegramClient = new OkHttpTelegramClient("7782840064:AAHCj2E3t1fy-drqh-d72iYu__EBTI2B8Gc");
    }
    @Override
    public void consume(Update update) {
        if(update.hasCallbackQuery()){
            Vozvrat(update.getCallbackQuery());
        }
        if(update.hasMessage()){
            String massegtext=update.getMessage().getText();
            Long getchatid = update.getMessage().getChatId();
            if(massegtext.equals("/start")){
                sendMainMenu(getchatid);
            }else{
                SendMessage message = SendMessage.builder().text("Гойда").chatId(getchatid).build();
                try {
                    telegramClient.execute(message);
                }catch(TelegramApiException e){
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void Vozvrat(CallbackQuery callbackQuery) {
        var data = callbackQuery.getData();
        var chatId = callbackQuery.getFrom().getId();
        switch (data){
            case "name" -> sendSpisok(chatId);
            case "day" -> sendDay(chatId);
            case "month" -> sendMonth(chatId);
            default -> sendText(chatId, "Не понял гойду");
        }
    }

    private void sendText(Long chatId, String massegtext) {
        SendMessage message = SendMessage.builder().text(massegtext).chatId(chatId).build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMonth(Long chatId) {
        sendText(chatId,"Гойда на месяц");
    }

    private void sendDay(Long chatId) {
        sendText(chatId,"Гойда на день");
    }

    private void sendSpisok(Long chatId) {
        sendText(chatId,"Гойда на человека");
    }

    private void  sendMainMenu(Long getchatid){
        SendMessage message = SendMessage.builder().text("Выбери гойду").chatId(getchatid).build();
        var button1 = InlineKeyboardButton.builder().text("Статистика за день").callbackData("day").build();
        var button2 = InlineKeyboardButton.builder().text("Статистика за месяц").callbackData("month").build();
        var button3 = InlineKeyboardButton.builder().text("Статистика за промежуток").callbackData("promeg").build();
        var button4 = InlineKeyboardButton.builder().text("Имена").callbackData("name").build();
        List<InlineKeyboardRow> keyboardRow = List.of(new InlineKeyboardRow(button1),new InlineKeyboardRow(button2),new InlineKeyboardRow(button3),new InlineKeyboardRow(button4));
        new InlineKeyboardRow();
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(keyboardRow);
        message.setReplyMarkup(markup);
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
