package gitactivity.main.repositories;

import gitactivity.main.model.UserDailyStat;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserDailyStatRepository extends JpaRepository<UserDailyStat, Long> {
    @Query("FROM UserDailyStat s WHERE s.login=:login")
    UserDailyStat findByLogin(@Param("login") String login);

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE statofuser", nativeQuery = true)
    void clearTable();
}
