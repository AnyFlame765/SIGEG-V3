package utez.edu.mx.smartfitutez.security.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utez.edu.mx.smartfitutez.models.exercises.Exercises;
import utez.edu.mx.smartfitutez.models.exercises.ExercisesRepository;

import javax.transaction.Transactional;

@Service
@Transactional
public class ExerciseService {

    @Autowired
    private ExercisesRepository repository;

    public boolean existsByName(String name){
        return repository.existsByName(name);
    }

    public void save(Exercises exercises){
        repository.save(exercises);
    }

    public Iterable<Exercises> list(){
        return repository.findAll();
    }

    public void delete(Long id){
        repository.deleteById(id);
    }

    public boolean existsById(Long id){
        return repository.existsById(id);
    }

    //by name
    public Exercises getByName(String name){
        return repository.findByName(name);
    }

    public Exercises getOne(Long id){
        return repository.findById(id).get();
    }

    public void update(Exercises exercises){
        repository.save(exercises);
    }

    public Exercises get(Long id){
        return repository.findById(id).get();
    }

    public Long getId(String name){
        return repository.findByName(name).getId();
    }
}
