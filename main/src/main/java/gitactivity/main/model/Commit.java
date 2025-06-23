package gitactivity.main.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Commit {
    private String id;
    private int repoId;
    private LocalDateTime date;
    private int changedLines;
    private String user;
    private String comment;


}
