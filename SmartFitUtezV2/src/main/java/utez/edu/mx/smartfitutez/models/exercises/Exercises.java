package utez.edu.mx.smartfitutez.models.exercises;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import utez.edu.mx.smartfitutez.models.routines.Routines;
import utez.edu.mx.smartfitutez.security.service.ExerciseService;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "exercises")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Exercises {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_exercises;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "category", nullable = false)
    private String category;
    @Column(name = "repeats", nullable = false)
    private String repeats;
    @Column(name = "url", nullable = false)
    private String url;

    //constructor
    public Exercises(String name, String category, String repeats, String url) {
        this.name = name;
        this.category = category;
        this.repeats = repeats;
        this.url = url;
    }

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Routines> routines;

    //get
    public Long getId() {
        return id_exercises;
    }

    public Exercises get() {
        return this;
    }


}
