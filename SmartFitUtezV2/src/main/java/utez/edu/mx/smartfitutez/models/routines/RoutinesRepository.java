package utez.edu.mx.smartfitutez.models.routines;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import utez.edu.mx.smartfitutez.security.entity.UserLR;
import utez.edu.mx.smartfitutez.models.exercises.Exercises;

import java.util.List;
import java.util.Optional;

public interface RoutinesRepository extends JpaRepository<Routines, Long> {
    @Query("SELECT u FROM UserLR u JOIN u.roles r WHERE r.id = 1 AND u.id = :id")
    Optional<UserLR> findUserByIdAndRole(Long id);

    @Query("SELECT e FROM Exercises e WHERE e.id_exercises = :exerciseId")
    Optional<Exercises> findByExerciseId(Long exerciseId);

    //listar rutinas por usuario y tambien traer el id del ejercicio
    @Query("SELECT r FROM Routines r WHERE r.userLR.id = :id")
    List<Routines> findRoutinesByUserId(@Param("id") Long id);


    Optional<Routines> findById(Long id);

    List<Routines> findAll();

    @Modifying
    @Query("DELETE FROM Routines r WHERE r.name = :name AND r.userLR.id = :id")
    void deleteRoutinesByNameAndUserId(@Param("name") String name, @Param("id") Long id);

    @Modifying
    @Query("DELETE FROM Routines r WHERE r.userLR = :user AND r.exercise = :exercise AND r.name = :name")
    void deleteByUserLRAndExerciseAndName(@Param("user") UserLR user, @Param("exercise") Exercises exercise, @Param("name") String name);

}
