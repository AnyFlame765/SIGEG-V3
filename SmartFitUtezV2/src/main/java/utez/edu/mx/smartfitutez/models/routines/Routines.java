package utez.edu.mx.smartfitutez.models.routines;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utez.edu.mx.smartfitutez.models.exercises.Exercises;
import utez.edu.mx.smartfitutez.security.entity.UserLR;


@Entity
@Table(name = "routines")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Routines {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_routines;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "id_exercise")
    private Exercises exercise;

    @ManyToOne
    @JoinColumn(name = "id_userLR")
    private UserLR userLR;
}
