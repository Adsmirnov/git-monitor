package gitactivity.main.model;

import lombok.Data;

import java.util.List;

@Data
public class Repository {
    private String link;
    private List<Commit> commits;
}
