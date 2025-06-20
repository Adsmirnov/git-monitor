package gitactivity.main.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "repositories")
@Data
public class Repository {
    @Column(name = "id")
    private int id;

    @Column(name = "url")
    private String url;

    @Column(name = "sha")
    private String sha;
}
