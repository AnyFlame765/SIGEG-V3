package utez.edu.mx.smartfitutez.security.controller;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import utez.edu.mx.smartfitutez.models.exercises.Exercises;
import utez.edu.mx.smartfitutez.models.routines.Routines;
import utez.edu.mx.smartfitutez.models.routines.RoutinesRepository;
import utez.edu.mx.smartfitutez.security.dto.JwtDto;
import utez.edu.mx.smartfitutez.security.dto.LoginUser;
import utez.edu.mx.smartfitutez.security.dto.Mensaje;
import utez.edu.mx.smartfitutez.security.dto.NewUser;
import utez.edu.mx.smartfitutez.security.entity.AdminRequest;
import utez.edu.mx.smartfitutez.security.entity.InstructorRequest;
import utez.edu.mx.smartfitutez.security.entity.Role;
import utez.edu.mx.smartfitutez.security.entity.UserLR;
import utez.edu.mx.smartfitutez.security.enums.RoleName;
import utez.edu.mx.smartfitutez.security.jwt.JwtProvider;
import utez.edu.mx.smartfitutez.security.repository.UserLrRepository;
import utez.edu.mx.smartfitutez.security.service.ExerciseService;
import utez.edu.mx.smartfitutez.security.service.RoleService;
import utez.edu.mx.smartfitutez.security.service.RoutineService;
import utez.edu.mx.smartfitutez.security.service.UserLrService;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(RoutineService.class);

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserLrService userLrService;

    @Autowired
    RoleService roleService;

    @Autowired
    JwtProvider jwtProvider;

    //Ejercicios
    @Autowired
    private ExerciseService service;

    //Rutinas
    @Autowired
    private RoutineService serviceRoutine;

    @Autowired
    private RoutinesRepository routinesRepository;

@Autowired
private JavaMailSender javaMailSender;

    @PostMapping("/nuevoInstructor")
    public ResponseEntity<?> newInstructor(@Valid @RequestBody InstructorRequest instructorRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new Mensaje("Campos mal o email invalido"), HttpStatus.BAD_REQUEST);
        }
        if (userLrService.existsByEmail(instructorRequest.getEmail())) {
            return new ResponseEntity<>(new Mensaje("Ese email ya existe"), HttpStatus.BAD_REQUEST);
        }

        UserLR userLR = new UserLR(
                instructorRequest.getName(),
                instructorRequest.getLast_name(),
                instructorRequest.getBirthday(),
                instructorRequest.getEmail(),
                passwordEncoder.encode(instructorRequest.getPassword())
        );

        Set<Role> roles = new HashSet<>();
        roles.add(roleService.getByRoleName(RoleName.ROLE_INSTRUCTOR).get());
        userLR.setRoles(roles);

        userLrService.save(userLR);

        return new ResponseEntity<>(new Mensaje("Instructor creado"), HttpStatus.CREATED);
    }

    //listar usaurios con rol de instructor
    @PostMapping("/nuevoUsuario")
    public ResponseEntity<?> newUser(@Valid @RequestBody NewUser newUser,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new Mensaje("Campos mal o email invalido"), HttpStatus.BAD_REQUEST);
        }
        if (!newUser.getEmail().endsWith("@utez.edu.mx")) {
            return new ResponseEntity<>(new Mensaje("Solo se pueden registrar usuarios con correo institucional"), HttpStatus.BAD_REQUEST);
        }
        if (userLrService.existsByEmail(newUser.getEmail())) {
            return new ResponseEntity<>(new Mensaje("Ese email ya existe"), HttpStatus.BAD_REQUEST);
        }

        UserLR userLRo = new UserLR(newUser.getName(), newUser.getLast_name(), newUser.getBirthday(), newUser.getCurrent_weight(), newUser.getHeight(), newUser.getGender(), newUser.getEmail(), passwordEncoder.encode(newUser.getPassword()));

        Set<Role> roles = new HashSet<>();
        roles.add(roleService.getByRoleName(RoleName.ROLE_USER).get());
        if (newUser.getRoles().contains("admin"))
            roles.add(roleService.getByRoleName(RoleName.ROLE_ADMIN).get());
        userLRo.setRoles(roles);
        userLrService.save(userLRo);

        return new ResponseEntity<>(new Mensaje("Usuario creado"), HttpStatus.CREATED);
    }
    @GetMapping("/listaAlumnos")
    public ResponseEntity<?> listAlumnos() {
        return new ResponseEntity<>(userLrService.findStudents(), HttpStatus.OK);
    }

    @GetMapping("/alumno/{id}")
    public ResponseEntity<?> getAlumnoById(@PathVariable Long id) {
        Optional<UserLR> alumnoOptional = userLrService.findStudentById(id);

        if (alumnoOptional.isPresent()) {
            UserLR alumno = alumnoOptional.get();
            return new ResponseEntity<>(alumno, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Mensaje("El alumno no existe"), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/updateAlumno/{id}")
    public ResponseEntity<?> updateAlumno(@PathVariable("id") Long id, @RequestBody UserLR updatedAlumno) {
        UserLR alumno = userLrService.updateStudent(id, updatedAlumno);

        if (alumno != null) {
            return new ResponseEntity<>(alumno, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Mensaje("El alumno no existe o no es válido"), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteAlumno/{id}")
    public ResponseEntity<?> deleteAlumnoById(@PathVariable Long id) {
        try {
            userLrService.deleteStudentById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addAdmin")
    public ResponseEntity newAdmin(@Valid @RequestBody AdminRequest adminRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new Mensaje("Campos mal o email invalido"), HttpStatus.BAD_REQUEST);
        }
        if (userLrService.existsByEmail(adminRequest.getEmail())) {
            return new ResponseEntity<>(new Mensaje("admin ya registrado"), HttpStatus.BAD_REQUEST);
        }

        UserLR userLR = new UserLR(adminRequest.getName(), adminRequest.getEmail(), passwordEncoder.encode(adminRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        roles.add(roleService.getByRoleName(RoleName.ROLE_ADMIN).get());
        userLR.setRoles(roles);
        userLrService.save(userLR);

        return new ResponseEntity<>(new Mensaje("admin creado"), HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUser loginUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("Campos mal"), HttpStatus.BAD_REQUEST);
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginUser.getEmail(),
                                loginUser.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());
        return new ResponseEntity<>(jwtDto, HttpStatus.OK);
    }


    //Ejercicios
    @PostMapping("/addExercise")
    public ResponseEntity<?> addExercise(@RequestBody Exercises exercise, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new Mensaje("Campos incompletos o mal"), HttpStatus.BAD_REQUEST);
        }
        if (service.existsByName(exercise.getName())) {
            return new ResponseEntity<>(new Mensaje("Ese ejercicio ya existe"), HttpStatus.BAD_REQUEST);
        }

        Exercises exercises = new Exercises(
                exercise.getName(),
                exercise.getCategory(),
                exercise.getRepeats(),
                exercise.getUrl()
        );
        service.save(exercises);
        return new ResponseEntity<>(new Mensaje("Ejercicio agregado"), HttpStatus.CREATED);
    }

    @GetMapping("/listExercise")
    public ResponseEntity<?> listExercise() {
        return new ResponseEntity<>(service.list(), HttpStatus.OK);
    }

    @GetMapping("/listExercise/{id}")
    public ResponseEntity<?> listExercise(@PathVariable("id") Long id) {
        if (!service.existsById(id))
            return new ResponseEntity<>(new Mensaje("No existe"), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(service.getOne(id).get(), HttpStatus.OK);
    }


    @DeleteMapping("/deleteExercise/{id}")
    public ResponseEntity<?> deleteExercise(@PathVariable("id") Long id) {
        if (!service.existsById(id))
            return new ResponseEntity<>(new Mensaje("No existe"), HttpStatus.NOT_FOUND);
        service.delete(id);
        return new ResponseEntity<>(new Mensaje("Ejercicio eliminado"), HttpStatus.OK);
    }

    @PutMapping("/updateExercise/{id}")
    public ResponseEntity<?> updateExercise(@PathVariable("id") Long id, @RequestBody Exercises exercise) {
        if (!service.existsById(id))
            return new ResponseEntity<>(new Mensaje("No existe"), HttpStatus.NOT_FOUND);
        Exercises exercises = service.getOne(id).get();
        exercises.setName(exercise.getName());
        exercises.setCategory(exercise.getCategory());
        exercises.setRepeats(exercise.getRepeats());
        exercises.setUrl(exercise.getUrl());
        service.save(exercises);
        return new ResponseEntity<>(new Mensaje("Ejercicio actualizado"), HttpStatus.OK);
    }

    //Instructores
    @GetMapping("/listInstructor")
    public ResponseEntity<?> listInstructor() {
        return new ResponseEntity<>(userLrService.findInstructors(), HttpStatus.OK);
    }

    @GetMapping("/instructor/{id}")
    public ResponseEntity<?> getInstructorById(@PathVariable Long id) {
        Optional<UserLR> instructorOptional = userLrService.findInstructorById(id);

        if (instructorOptional.isPresent()) {
            UserLR instructor = instructorOptional.get();
            return new ResponseEntity<>(instructor, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Mensaje("El instructor no existe"), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/updateInstructor/{id}")
    public ResponseEntity<?> updateInstructor(@PathVariable("id") Long id, @RequestBody UserLR updatedInstructor) {
        UserLR instructor = userLrService.updateInstructor(id, updatedInstructor);

        if (instructor != null) {
            return new ResponseEntity<>(instructor, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Mensaje("El instructor no existe o no es válido"), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteInstructor/{id}")
    public ResponseEntity<?> deleteInstructorById(@PathVariable Long id) {
        try {
            userLrService.deleteInstructorById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addRoutine")
    @ResponseBody
    public Routines addRoutineToUser(@RequestBody Routines routine) {
        Long userId = routine.getUserLR().getId();
        Long exerciseId = routine.getExercise().getId_exercises();
        String name = routine.getName();
        String description = routine.getDescription();
        Optional<Exercises> exercise = routinesRepository.findByExerciseId(exerciseId);
        Optional<UserLR> user = routinesRepository.findUserByIdAndRole(userId);
        if (!exercise.isPresent()) {
            logger.error("Exercise with id {} not found.", exerciseId);
            throw new RuntimeException("Exercise with id " + exerciseId + " not found.");
        }
        if (!user.isPresent()) {
            logger.error("User with id {} and role 'Usuario' not found.", userId);
            throw new RuntimeException("User with id " + userId + " and role 'Usuario' not found.");
        }
        Routines newRoutine = new Routines();
        newRoutine.setName(name);
        newRoutine.setDescription(description);
        newRoutine.setExercise(exercise.get());
        newRoutine.setUserLR(user.get());
        return routinesRepository.save(newRoutine);
    }

    @DeleteMapping("/deleteRoutine/{routineId}")
    public ResponseEntity<Void> deleteRoutine(@PathVariable Long routineId) {
        try {
            serviceRoutine.deleteRoutineById(routineId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.error("Error deleting routine with id {}", routineId, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/updateRoutine/{routineId}")
    public ResponseEntity<Routines> updateRoutine(@PathVariable Long routineId,
                                                  @RequestBody Routines updatedRoutine) {
        try {
            Routines routine = serviceRoutine.updateRoutine(routineId, updatedRoutine);
            return ResponseEntity.ok(routine);
        } catch (RuntimeException e) {
            logger.error("Error updating routine with id {}", routineId, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{id}")
    public List<Routines> getRoutinesByUserIdAndRoleId(@PathVariable Long id) {
        return serviceRoutine.getRoutinesByUserId(id);
    }

    @DeleteMapping("/{id}/{routineName}")
    public void deleteRoutinesByNameAndUserId(@PathVariable Long id, @PathVariable String routineName) {
        serviceRoutine.deleteRoutinesByNameAndUserId(routineName, id);
    }

    //servicio que liste todos los ejercicios que hay dentro de una rutina de cada usuario


    @DeleteMapping("/{userId}/{exerciseId}/{routineName}")
    public ResponseEntity<String> deleteRoutinesByUserExerciseAndName(@PathVariable Long userId,
                                                                      @PathVariable Long exerciseId,
                                                                      @PathVariable String routineName) {
        serviceRoutine.deleteRoutinesByUserExerciseAndName(userId, exerciseId, routineName);
        return ResponseEntity.ok("Routines deleted successfully.");
    }


}
