package gitactivity.main.model;

import jakarta.persistence.Entity;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Repository {
    private String link;
    private List<Commit> commits;
}
