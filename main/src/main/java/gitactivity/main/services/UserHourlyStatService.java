package gitactivity.main.services;


import gitactivity.main.model.UserHourlyStat;
import gitactivity.main.repositories.UserHourlyStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserHourlyStatService {
    @Autowired
    private UserHourlyStatRepository userHourlyStatRepository;

    public UserHourlyStat saveUserHourlyStat(UserHourlyStat stat) {  // Метод для создания и заполнения новой ячейки в базе данных
        return userHourlyStatRepository.save(stat);
    }

    public List<UserHourlyStat> getUserHourlyStat(String login, LocalDateTime date) {  // Метод для получения статистики данного пользователя за данный час данного дня
        return userHourlyStatRepository.findByLoginAndDateAndHour(login, date, date.getHour());
    }
}
