package gitactivity.main.model;

import lombok.Data;

import java.util.List;

@Data
public class Admin {
    private String login;
    private String password;
    private List<User> users;
}
