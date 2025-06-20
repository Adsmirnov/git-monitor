package gitactivity.main.services;

import gitactivity.main.model.Repository;
import gitactivity.main.repositories.RepositoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RepositoryService {

    @Autowired
    private RepositoryRepository repositoryRepository;

    public List<Repository> getRepositories() {  // Метод для получения списка всех репозиториев из базы данных
        return repositoryRepository.findAll();
    }

    public Optional<Repository> getRepositoryById(Long id) { // Метод для поиска репозитория по айди
        return repositoryRepository.findById(id);
    }

    @Transactional
    public Repository createRepository(Repository repository) {  // Метод для создания ячейки с репозиторием в базе данных
        return repositoryRepository.save(repository);
    }

}
