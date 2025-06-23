package gitactivity.main.javabot;

import gitactivity.main.model.User;
import gitactivity.main.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class UpdateSoob implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;

    private Map<String, String> env = System.getenv();

    @Autowired
    private UserService userService;

    public UpdateSoob() {
        this.telegramClient = new OkHttpTelegramClient(env.get("TG_BOT_TOKEN"));
    }

    private boolean checkWhitelist(String username) {
        for(User user : userService.getUsers()) {
            if (user.getLogin().equals(username)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void consume(Update update) {
        if(update.hasCallbackQuery()){
            // проверка на вайтлист
            String username = update.getCallbackQuery().getFrom().getUserName();
            if (!checkWhitelist(username)) {
                return;
            }

            Vozvrat(update.getCallbackQuery());
        }
        if(update.hasMessage()){
            String massegtext=update.getMessage().getText();
            Long getchatid = update.getMessage().getChatId();

            // проверка на вайтлист
            String username = update.getMessage().getFrom().getUserName();
            if (!checkWhitelist(username)) {
                return;
            }

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
            default -> {if (data.startsWith("name_")){
                namegraff(chatId, data);
            }else{
                sendText(chatId, "Не понял гойду");
            } }
        }
    }

    private void namegraff(Long chatId, String data) {
        SendMessage message = SendMessage.builder().text("Объект для гойдирования " + data.substring(5)).chatId(chatId).build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
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
        String [] names = new String[] {"Иван", "Игорь"};
        nameButton(chatId,names);
    }

    private void nameButton(Long chatId, String[] names) {
        SendMessage message = SendMessage.builder().text("Выберите сотрудника").chatId(chatId).build();
        List<InlineKeyboardRow> buttons = new ArrayList<>();
        for (String name : names) {
            var button = InlineKeyboardButton.builder().text(name).callbackData("name_" + name).build();
            buttons.add( new InlineKeyboardRow(button) );
        }
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);
        message.setReplyMarkup(markup);
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
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
