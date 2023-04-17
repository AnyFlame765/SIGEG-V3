package utez.edu.mx.smartfitutez.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import utez.edu.mx.smartfitutez.models.routines.Routines;
import utez.edu.mx.smartfitutez.security.entity.InstructorRequest;
import utez.edu.mx.smartfitutez.security.entity.UserLR;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserLrRepository extends JpaRepository<UserLR, Long> {
    Optional<UserLR> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByName(String name);

    //listar todos los usuarios
    List<UserLR> findAll();

    //listar usuarios con el rol de Instructor
    @Query("SELECT u FROM UserLR u JOIN u.roles roles WHERE roles.id = 2")
    List<UserLR> findInstructors();

    @Query("SELECT u FROM UserLR u JOIN u.roles roles WHERE roles.id = 2 AND u.id = :id")
    Optional<UserLR> findByIdAndInstructorRole(Long id);

    //listar usuarios con el rol de Alumno
    @Query("SELECT u FROM UserLR u JOIN u.roles roles WHERE roles.id = 1")
    List<UserLR> findStudents();

    @Query("SELECT u FROM UserLR u JOIN u.roles roles WHERE roles.id = 1 AND u.id = :id")
    Optional<UserLR> findByIdAndAlumnoRole(Long id);


}
