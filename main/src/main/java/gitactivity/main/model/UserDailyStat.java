package gitactivity.main.model;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "statofuser")
@Data
public class UserDailyStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "commits")
    private int commits;

    @Column(name = "lines")
    private int lines;

    @Column(name = "date")
    private LocalDateTime date;
}
