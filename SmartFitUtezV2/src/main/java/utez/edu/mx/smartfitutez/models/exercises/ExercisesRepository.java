package utez.edu.mx.smartfitutez.models.exercises;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExercisesRepository extends JpaRepository<Exercises, Long> {
    boolean existsByName(String name);
    Exercises findByName(String name);
}
