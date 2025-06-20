package gitactivity.main.repositories;

import gitactivity.main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findUserByLogin(String login);

}
