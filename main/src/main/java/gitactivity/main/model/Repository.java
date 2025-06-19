package gitactivity.main.model;

import lombok.Data;

import java.util.List;

@Data
public class Repository {
    private int id;
    private String url;
    private String sha;
}
