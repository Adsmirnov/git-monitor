package gitactivity.main.services;


import gitactivity.main.api.CommitParserService;
import gitactivity.main.model.Commit;
import gitactivity.main.model.UserHourlyStat;
import gitactivity.main.repositories.UserHourlyStatRepository;
import lombok.RequiredArgsConstructor;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class UserHourlyStatService {
    Logger logger = Logger.getLogger(getClass().getName());


    private UserHourlyStatRepository userHourlyStatRepository;

    private CommitParserService commitParserService;

    public UserHourlyStatService(UserHourlyStatRepository userHourlyStatRepository, CommitParserService commitParserService) {
        this.userHourlyStatRepository = userHourlyStatRepository;
        this.commitParserService = commitParserService;
    }

    private ArrayList<String> users = new ArrayList<>();

    // ({Пользователь, время}, коммиты)
    private HashMap<Pair<String, LocalDateTime>, ArrayList<Commit>> cachedCommits = new HashMap<>();

    public void saveUserHourlyStat() {  // Метод для создания и заполнения новой ячейки в базе данных
        userHourlyStatRepository.clearTable();

        Map<String, ArrayList<Commit>> commits = commitParserService.getParsedCommits(LocalDateTime.now().minusHours(1), LocalDateTime.now());

        Set<String> setKeys = commits.keySet();
        users.addAll(setKeys);

        for (String user : users) {
            UserHourlyStat stat = new UserHourlyStat();
            ArrayList<Commit> commitsOfUser = commits.get(user);

            if (commitsOfUser == null) {
                return;
            }

            int sumOfChangedLines = 0;
            for (Commit c : commitsOfUser) {
                sumOfChangedLines += c.getChangedLines();
            }

            stat.setLogin(user);
            stat.setCommits(commitsOfUser.size());
            stat.setLines(sumOfChangedLines);
            stat.setDate(LocalDateTime.now());

            userHourlyStatRepository.save(stat);
        }
    }

    public UserHourlyStat[] getUserHourlyStats(String login) {
        logger.info("Получаю статистику за час");
        UserHourlyStat[] list = new UserHourlyStat[10];

        LocalDateTime since = LocalDate.now().atTime(LocalTime.of(9, 0));


        for (int i = 0; i < list.length; i++) {
            logger.info("Получаю статистику за " + (i + 9) + " час");

            Pair<String, LocalDateTime> currentUser = new Pair<>(login, since.plusHours(i));
            ArrayList<Commit> commitsOfUserByHour;
            if (LocalDateTime.now().getHour() < (i + 9)) {
                commitsOfUserByHour = null;
            } else if (!cachedCommits.containsKey(currentUser) || LocalDateTime.now().getHour() == since.plusHours(i).getHour()) {
                Map<String, ArrayList<Commit>> commits = commitParserService.getParsedCommits(since.plusHours(i), since.plusHours(i + 1));
                commitsOfUserByHour = commits.get(login);
                cachedCommits.put(currentUser, commitsOfUserByHour);
            } else {
                commitsOfUserByHour = cachedCommits.get(currentUser);
            }

            UserHourlyStat stat = new UserHourlyStat();
            if (commitsOfUserByHour == null) {
                stat.setCommits(0);
                stat.setDate(since.plusHours(i));
                stat.setLines(0);
                stat.setLogin(login);

                list[i] = stat;
            } else {
                int sumOfChangedLines = 0;
                for (Commit c : commitsOfUserByHour) {
                    sumOfChangedLines += c.getChangedLines();
                }

                stat.setLogin(login);
                stat.setDate(since.plusHours(i));
                stat.setLines(sumOfChangedLines);
                stat.setCommits(commitsOfUserByHour.size());

                list[i] = stat;
            }
        }

        logger.info("Статистика получина");

        return list;
    }
}



