package gitactivity.main.model;

import lombok.Data;

import java.util.List;

@Data
public class User {
    private String login;
    private String password;
    private List<Commit> userCommits;
    private List<User> adminUsers;

}
