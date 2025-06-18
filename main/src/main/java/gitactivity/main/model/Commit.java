package gitactivity.main.model;

import lombok.Data;

import java.sql.Time;
import java.util.Date;

@Data
public class Commit {
    private Time time;
    private Date date;
    private String branch;
    private int changedLines;
    private String user;
    private String comment;


}
