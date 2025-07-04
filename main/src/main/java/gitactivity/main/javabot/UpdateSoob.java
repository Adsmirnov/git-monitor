package gitactivity.main.javabot;

import gitactivity.main.api.GitApiService;
import gitactivity.main.charts.PictureManager;
import gitactivity.main.model.User;
import gitactivity.main.model.UserDailyStat;
import gitactivity.main.services.UserDailyStatService;
import gitactivity.main.services.UserHourlyStatService;
import gitactivity.main.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@EnableScheduling
@Component
public class UpdateSoob implements LongPollingSingleThreadUpdateConsumer {
    Logger logger = Logger.getLogger(getClass().getName());
    private final TelegramClient telegramClient;

    private Map<String, String> env = System.getenv();

    private final UserService userService;

    private final UserDailyStatService userDailyStatService;

    private final GitApiService gitApiService;

    private final PictureManager pictureManager;

    private final UserHourlyStatService userHourlyStatService;

    public UpdateSoob(UserService userService, UserDailyStatService userDailyStatService, GitApiService gitApiService, PictureManager pictureManager, UserHourlyStatService userHourlyStatService) {

        this.telegramClient =  new OkHttpTelegramClient(env.get("TG_BOT_TOKEN"));
        this.userService = userService;
        this.userDailyStatService = userDailyStatService;
        this.gitApiService = gitApiService;
        this.pictureManager = pictureManager;
        this.userHourlyStatService = userHourlyStatService;
    }



    private boolean checkWhitelist(String username) {
        for (User user : userService.getUsers()) {
            if (user.getLogin().equals(username)) {
                return true;
            }
        }
        return false;
    }

    private void setGroupLink(String username) {
        if (!checkWhitelist(username)) {
            return;
        }
        for (User user : userService.getUsers()) {
            if (user.getLogin().equals(username)) {
                gitApiService.setGroupLink(user.getGroup());
            }
        }
    }

    @Override
    public void consume(Update update) {
        if (update.hasCallbackQuery()) {
            // проверка на вайтлист
            String username = update.getCallbackQuery().getFrom().getUserName();
            if (!checkWhitelist(username)) {
                return;
            }
            setGroupLink(username);
            try {
                vozvrat(update.getCallbackQuery());
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (update.hasMessage()) {
            String messagetext = update.getMessage().getText();
            Long getchatid = update.getMessage().getChatId();

            // проверка на вайтлист
            String username = update.getMessage().getFrom().getUserName();
            if (!checkWhitelist(username)) {
                return;
            }
            setGroupLink(username);
            if (messagetext.equals("/start")) {
                sendMainMenu(getchatid);
            } else {
                SendMessage message = SendMessage.builder().text("Гойда").chatId(getchatid).build();
                try {
                    telegramClient.execute(message);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void vozvrat(CallbackQuery callbackQuery) throws IOException, InterruptedException {
        var data = callbackQuery.getData();
        var chatId = callbackQuery.getFrom().getId();
        switch (data) {
            case "name" -> sendSpisok(chatId);
            case "day" -> sendDay(chatId);
            case "month" -> sendMonth(chatId);
            default -> {
                if (data.startsWith("name_")) {
                    namegraff(chatId, data);
                } else {
                    sendText(chatId, "Не понял гойду");
                }
            }
        }
    }

    private void namegraff(Long chatId, String data) throws IOException, InterruptedException {
        pictureManager.drawUserHourlyStats(data.substring(5));
        ClassPathResource resource = new ClassPathResource("static/goida.png");
        InputStream inputStream = resource.getInputStream();
        InputFile inputFile = new InputFile(inputStream, "goida.png");
        SendPhoto sendPhoto = SendPhoto.builder()
                .chatId(chatId)
                .photo(inputFile)
                .build();
        try {
            telegramClient.execute(sendPhoto);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        pictureManager.deletePicture("main/target/classes/static/goida.png");
    }


    private void sendText(Long chatId, String messagetext) {
        SendMessage message = SendMessage.builder().text(messagetext).chatId(chatId).build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMonth(Long chatId) {
        sendText(chatId, "Гойда на месяц");
    }

    @Scheduled(cron = "0 15 11 * * MON-FRI")
    private void send() throws IOException {
        List<User> users = userService.getUsers();
        for (User user : users) {
            String chatId = user.getChatId();
            if (chatId != null) {
                gitApiService.setGroupLink(user.getGroup());
                sendDay(Long.parseLong(chatId.substring(0, chatId.length() - 1)));
            }
        }
    }

    private void sendDay(Long chatId) throws IOException {
        userDailyStatService.saveUserDailyStat();
        logger.info(chatId.toString());
        pictureManager.drawDailyStats();
        ClassPathResource resource = new ClassPathResource("static/goida.png");
        InputStream inputStream = resource.getInputStream();
        InputFile inputFile = new InputFile(inputStream, "goida.png");
        SendPhoto sendPhoto = SendPhoto.builder()
                .chatId(chatId)
                .photo(inputFile)
                .build();
        try {
            telegramClient.execute(sendPhoto);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        pictureManager.deletePicture("main/target/classes/static/goida.png");
    }

    private void sendSpisok(Long chatId) {
        List<UserDailyStat> dailyStats = userDailyStatService.getStats();
        ArrayList<String> names = new ArrayList<>();
        for (UserDailyStat stat : dailyStats) {
            names.add(stat.getLogin());
        }
        nameButton(chatId, names);
    }

    private void nameButton(Long chatId, ArrayList<String> names) {
        SendMessage message = SendMessage.builder().text("Выберите сотрудника").chatId(chatId).build();
        HashSet<InlineKeyboardRow> buttons = new HashSet<>();
        for (String name : names) {
            var button = InlineKeyboardButton.builder().text(name).callbackData("name_" + name).build();
            buttons.add(new InlineKeyboardRow(button));
        }
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons.stream().toList());
        message.setReplyMarkup(markup);
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMainMenu(Long getchatid) {
        SendMessage message = SendMessage.builder().text("Выберите действие").chatId(getchatid).build();
        var button1 = InlineKeyboardButton.builder().text("Статистика за день").callbackData("day").build();
        var button4 = InlineKeyboardButton.builder().text("Имена").callbackData("name").build();
        List<InlineKeyboardRow> keyboardRow = List.of(new InlineKeyboardRow(button1), new InlineKeyboardRow(button4));
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
