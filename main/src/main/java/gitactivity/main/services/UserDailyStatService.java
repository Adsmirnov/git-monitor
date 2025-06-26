package gitactivity.main.services;


import gitactivity.main.api.CommitParserService;
import gitactivity.main.model.Commit;
import gitactivity.main.model.UserDailyStat;
import gitactivity.main.repositories.UserDailyStatRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class UserDailyStatService {


    private final UserDailyStatRepository userDailyStatRepository;

    private final CommitParserService commitParserService;

    public UserDailyStatService(UserDailyStatRepository userDailyStatRepository, CommitParserService commitParserService) {
        this.userDailyStatRepository = userDailyStatRepository;
        this.commitParserService = commitParserService;
    }

    private ArrayList<String> users = new ArrayList<>();

    @Transactional
    public UserDailyStat saveDailyStat(UserDailyStat stat) {  // Метод для создания и заполнения новой ячейке в базе данных
        return userDailyStatRepository.save(stat);
    }

    public UserDailyStat getUserDailyStat(String login) {  // Метод для получения статистики данного пользователя за день
        return userDailyStatRepository.findByLogin(login);
    }

    public List<UserDailyStat> getStats() {
        return userDailyStatRepository.findAll();
    }

    public void saveUserDailyStat() {
        userDailyStatRepository.clearTable();
        Map<String, ArrayList<Commit>> commits = commitParserService.getParsedCommits(LocalDate.now().atTime(LocalTime.of(9, 0)), LocalDate.now().atTime(LocalTime.of(18, 0)));

        Set<String> setKeys = commits.keySet();
        users.addAll(setKeys);

        for (String user : users) {
            UserDailyStat stat = new UserDailyStat();
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

            userDailyStatRepository.save(stat);
        }
    }


}
