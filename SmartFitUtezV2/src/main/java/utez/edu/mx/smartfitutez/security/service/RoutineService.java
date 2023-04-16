package utez.edu.mx.smartfitutez.security.service;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utez.edu.mx.smartfitutez.models.exercises.Exercises;
import utez.edu.mx.smartfitutez.models.exercises.ExercisesRepository;
import utez.edu.mx.smartfitutez.models.routines.Routines;
import utez.edu.mx.smartfitutez.models.routines.RoutinesRepository;
import utez.edu.mx.smartfitutez.security.entity.UserLR;
import utez.edu.mx.smartfitutez.security.repository.UserLrRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoutineService {

    //logger para ver los errores
    private static final Logger logger = LoggerFactory.getLogger(RoutineService.class);

    @Autowired
    private RoutinesRepository repository;

    @Autowired
    private UserLrRepository userRepository;

    @Autowired
    private ExercisesRepository exerciseRepository;

    @Autowired
    private RoutinesRepository routinesRepository;

    public Object list() {
        return repository.findAll();
    }

    public Routines addRoutineToUser(Long userId, Long exerciseId, String name, String description) {
        Optional<Exercises> exercise = routinesRepository.findByExerciseId(exerciseId);
        Optional<UserLR> user = routinesRepository.findUserByIdAndRole(userId); // busca al usuario por su id y su rol de usuario

        if (!exercise.isPresent()) {
            logger.error("Exercise with id {} not found.", exerciseId);
            throw new RuntimeException("Exercise with id " + exerciseId + " not found.");
        }

        if (!user.isPresent()) {
            logger.error("User with id {} and role 'Usuario' not found.", userId);
            throw new RuntimeException("User with id " + userId + " and role 'Usuario' not found.");
        }

        Routines routine = new Routines();
        routine.setName(name);
        routine.setDescription(description);
        routine.setExercise(exercise.get());
        routine.setUserLR(user.get());

        return routinesRepository.save(routine);
    }

    public void deleteRoutineById(Long id) {
        Optional<Routines> routine = routinesRepository.findById(id);

        if (!routine.isPresent()) {
            logger.error("Routine with id {} not found.", id);
            throw new RuntimeException("Routine with id " + id + " not found.");
        }

        routinesRepository.delete(routine.get());
    }

    public Routines updateRoutine(Long id, Routines updatedRoutine) {
        Optional<Routines> routine = routinesRepository.findById(id);

        if (!routine.isPresent()) {
            logger.error("Routine with id {} not found.", id);
            throw new RuntimeException("Routine with id " + id + " not found.");
        }

        Routines existingRoutine = routine.get();
        existingRoutine.setName(updatedRoutine.getName());
        existingRoutine.setDescription(updatedRoutine.getDescription());

        return routinesRepository.save(existingRoutine);
    }

    public Routines getRoutineById(Long id) {
        Optional<Routines> routine = routinesRepository.findById(id);

        if (!routine.isPresent()) {
            logger.error("Routine with id {} not found.", id);
            throw new RuntimeException("Routine with id " + id + " not found.");
        }

        return routine.get();
    }

    public List<Routines> findAllRoutinesByUserIdAndRoleId(Long id) {
        Optional<UserLR> user = userRepository.findByIdAndAlumnoRole(id);
        if (user.isEmpty()) {
            logger.error("User with id {} and role 'Usuario' not found.", id);
            throw new RuntimeException("User with id " + id + " and role 'Usuario' not found.");
        }
        return routinesRepository.findAllRoutinesByUserIdAndRoleId(id);
    }

    @Transactional
    public void deleteRoutinesByNameAndUserId(String name, Long id) {
        Optional<UserLR> user = userRepository.findByIdAndAlumnoRole(id);
        if (user.isEmpty()) {
            logger.error("User with id {} and role 'Usuario' not found.", id);
            throw new RuntimeException("User with id " + id + " and role 'Usuario' not found.");
        }
        routinesRepository.deleteRoutinesByNameAndUserId(name, id);
    }

    public void deleteRoutinesByUserExerciseAndName(Long userId, Long exerciseId, String name) {
        Optional<Exercises> exercise = routinesRepository.findByExerciseId(exerciseId);
        Optional<UserLR> user = userRepository.findByIdAndAlumnoRole(userId);

        if (exercise.isEmpty()) {
            logger.error("Exercise with id {} not found.", exerciseId);
            throw new RuntimeException("Exercise with id " + exerciseId + " not found.");
        }

        if (user.isEmpty()) {
            logger.error("User with id {} and role 'Usuario' not found.", userId);
            throw new RuntimeException("User with id " + userId + " and role 'Usuario' not found.");
        }

        routinesRepository.deleteByUserLRAndExerciseAndName(user.get(), exercise.get(), name);
    }

}
