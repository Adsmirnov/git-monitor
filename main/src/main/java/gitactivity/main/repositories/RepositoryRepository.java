package gitactivity.main.repositories;

import gitactivity.main.model.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RepositoryRepository extends JpaRepository<Repository, Long> {

}
