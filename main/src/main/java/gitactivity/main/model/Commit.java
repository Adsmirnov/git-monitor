package gitactivity.main.model;

import lombok.Data;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class Commit {
    private String id;
    private int repoId;
    private LocalDateTime date;
    private int changedLines;
    private String user;
    private String comment;


}
