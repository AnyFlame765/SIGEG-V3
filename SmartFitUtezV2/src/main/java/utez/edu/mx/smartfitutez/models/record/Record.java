package utez.edu.mx.smartfitutez.models.record;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import utez.edu.mx.smartfitutez.security.entity.UserLR;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;

@Entity
@Table(name = "record")
@AllArgsConstructor
@NoArgsConstructor
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_record;

    @Column(name = "weight", nullable = false)
    private String weight;

    @Column(name = "date", nullable = false)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "id_userLR")
    private UserLR userLR;
}
