package gitactivity.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GitApiService {

    @Autowired
    GitApiRepository gitApiRepository;

    public String getProcessedData() {
        String rawApiData = gitApiRepository.getGitData();
        return rawApiData;
    }

}
