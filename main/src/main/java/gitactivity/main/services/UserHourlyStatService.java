package gitactivity.main.services;


import gitactivity.main.api.CommitParserService;
import gitactivity.main.model.Commit;
import gitactivity.main.model.UserHourlyStat;
import gitactivity.main.repositories.UserHourlyStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserHourlyStatService {
    @Autowired
    private UserHourlyStatRepository userHourlyStatRepository;

    @Autowired
    private CommitParserService commitParserService;

    public void saveUserHourlyStat() {  // Метод для создания и заполнения новой ячейки в базе данных
        userHourlyStatRepository.clearTable();
        UserHourlyStat stat = new UserHourlyStat();
        ArrayList<Commit> commits = commitParserService.getParsedCommits(LocalDateTime.now().minusHours(5), LocalDateTime.now());
        int sumOfChangedLines = 0;
        System.out.println(commits);
        for (Commit commit : commits) {
            sumOfChangedLines+=commit.getChangedLines();
        }

        stat.setLogin("mileZ239");
        stat.setCommits(commits.size());
        stat.setLines(sumOfChangedLines);
        stat.setDate(LocalDateTime.now());

        userHourlyStatRepository.save(stat);

    }

    public List<UserHourlyStat> getUserHourlyStat(String login, LocalDateTime date) {  // Метод для получения статистики данного пользователя за данный час данного дня
        return userHourlyStatRepository.findByLoginAndDateAndHour(login, date, date.getHour());
    }
}
