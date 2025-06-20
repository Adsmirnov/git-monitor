package gitactivity.main.services;


import gitactivity.main.model.UserDailyStat;
import gitactivity.main.repositories.UserDailyStatRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDailyStatService {

    @Autowired
    private UserDailyStatRepository userDailyStatRepository;

    @Transactional
    public UserDailyStat saveDailyStat(UserDailyStat stat) {  // Метод для создания и заполнения новой ячейке в базе данных
        return userDailyStatRepository.save(stat);
    }

    public List<UserDailyStat> getUserDailyStat(String login, LocalDateTime date) {  // Метод для получения статистики данного пользователя за день
        return userDailyStatRepository.findByLoginAndDate(login, date);
    }


}
