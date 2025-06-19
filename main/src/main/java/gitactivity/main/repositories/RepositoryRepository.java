package gitactivity.main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface RepositoryRepository extends JpaRepository<Repository, Long> {
}
