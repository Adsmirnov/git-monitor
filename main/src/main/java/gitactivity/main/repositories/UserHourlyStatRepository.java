package gitactivity.main.repositories;

import gitactivity.main.model.UserHourlyStat;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserHourlyStatRepository extends JpaRepository<UserHourlyStat, Long> {
    List<UserHourlyStat> findByLogin(String login);

    @Query("SELECT s FROM UserHourlyStat s WHERE s.login = :login AND DATE(s.date) = DATE(:date) AND HOUR(s.date) = :hour")
    List<UserHourlyStat> findByLoginAndDateAndHour(
            @Param("login") String login,
            @Param("date") LocalDateTime date,
            @Param("hour") int hour
    );

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE statofuserbyhour", nativeQuery = true)
    void clearTable();
}
