package gitactivity.main.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.sql.Time;
import java.util.Date;


@Entity
@Data
public class Commit {
    @Id
    private int id;

    private Time time;
    private Date date;
    private String branch;
    private int changedLines;
    private String user;
    private String comment;


}
