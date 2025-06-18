package gitactivity.main.model;

import jakarta.persistence.Entity;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class User {
    private String login;
    private String password;
    private List<Commit> userCommits;

}
