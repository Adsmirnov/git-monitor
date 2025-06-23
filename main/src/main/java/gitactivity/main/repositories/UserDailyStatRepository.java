package gitactivity.main.repositories;

import gitactivity.main.model.UserDailyStat;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserDailyStatRepository extends JpaRepository<UserDailyStat, Long> {
    List<UserDailyStat> findByLogin(String login);

    @Query("SELECT s FROM UserDailyStat s WHERE s.login = :login AND DATE(s.date) = DATE(:date)")
    List<UserDailyStat> findByLoginAndDate(@Param("login") String login, @Param("date") LocalDateTime date);

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE statofuser", nativeQuery = true)
    void clearTable();
}
