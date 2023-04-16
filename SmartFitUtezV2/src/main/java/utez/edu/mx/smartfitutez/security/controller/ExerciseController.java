package utez.edu.mx.smartfitutez.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.smartfitutez.models.exercises.Exercises;
import utez.edu.mx.smartfitutez.security.dto.Mensaje;
import utez.edu.mx.smartfitutez.security.service.ExerciseService;


@RestController
@RequestMapping("/exercise")
@CrossOrigin

public class ExerciseController {
    @Autowired
    private ExerciseService service;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<?> addExercise(@RequestBody Exercises exercise, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new Mensaje("Campos incompletos o mal"), HttpStatus.BAD_REQUEST);
        }
        if(service.existsByName(exercise.getName())) {
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

     @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<?> listExercise(){
        return new ResponseEntity<>(service.list(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteExercise(@PathVariable("id") Long id){
        if(!service.existsById(id))
            return new ResponseEntity<>(new Mensaje("No existe"), HttpStatus.NOT_FOUND);
        service.delete(id);
        return new ResponseEntity<>(new Mensaje("Ejercicio eliminado"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateExercise(@PathVariable("id") Long id, @RequestBody Exercises exercise, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity<>(new Mensaje("Campos incompletos o mal"), HttpStatus.BAD_REQUEST);
        if(!service.existsById(id))
            return new ResponseEntity<>(new Mensaje("No existe"), HttpStatus.NOT_FOUND);
        if(service.existsByName(exercise.getName()) && service.getByName(exercise.getName()).get().getId() != id)
            return new ResponseEntity<>(new Mensaje("Ese ejercicio ya existe"), HttpStatus.BAD_REQUEST);

        Exercises exercises = service.getOne(id).get();
        exercises.setName(exercise.getName());
        exercises.setCategory(exercise.getCategory());
        exercises.setRepeats(exercise.getRepeats());
        exercises.setUrl(exercise.getUrl());
        service.save(exercises);
        return new ResponseEntity<>(new Mensaje("Ejercicio actualizado"), HttpStatus.OK);
    }
}

