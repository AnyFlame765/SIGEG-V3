package utez.edu.mx.smartfitutez.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import utez.edu.mx.smartfitutez.models.exercises.Exercises;
import utez.edu.mx.smartfitutez.security.entity.UserLR;
import utez.edu.mx.smartfitutez.security.repository.UserLrRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserLrService {
    @Autowired
    UserLrRepository userLrRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public Optional<UserLR> getByEmail(String email){
        return userLrRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email){
        return userLrRepository.existsByEmail(email);
    }

    public boolean existsByName(String name){
        return userLrRepository.existsByName(name);
    }

    public void save(UserLR userLR){
        userLrRepository.save(userLR);
    }

    public boolean existsById(Long id){
        return userLrRepository.existsById(id);
    }


    public void delete(Long id){
        userLrRepository.deleteById(id);
    }

    public UserLR getOne(Long id){
        return userLrRepository.findById(id).get();
    }

    //list
    public Iterable<UserLR> list(){
        return userLrRepository.findAll();
    }

    //listar usuarios con el rol de Instructor
    public List<UserLR> findInstructors(){
        return userLrRepository.findInstructors();
    }

    public UserLR updateInstructor(Long id, UserLR updatedInstructor) {
        Optional<UserLR> instructorOptional = userLrRepository.findByIdAndInstructorRole(id);

        if (instructorOptional.isPresent()) {
            UserLR instructor = instructorOptional.get();
            instructor.setName(updatedInstructor.getName());
            instructor.setLast_name(updatedInstructor.getLast_name());
            instructor.setBirthday(updatedInstructor.getBirthday());
            instructor.setEmail(updatedInstructor.getEmail());
            String encodedPassword = passwordEncoder.encode(updatedInstructor.getPassword());
            instructor.setPassword(encodedPassword);
            return userLrRepository.save(instructor);
        } else {
            return null;
        }
    }

    public Optional<UserLR> findInstructorById(Long id) {
        return userLrRepository.findByIdAndInstructorRole(id);
    }

    public void deleteInstructorById(Long id) {
        Optional<UserLR> instructorOptional = userLrRepository.findByIdAndInstructorRole(id);

        if (instructorOptional.isPresent()) {
            UserLR instructor = instructorOptional.get();
            userLrRepository.delete(instructor);
        } else {
            System.out.println("No se encontro el instructor");
        }
    }

    //listar usuarios con el rol de Alumno
    public List<UserLR> findStudents(){
        return userLrRepository.findStudents();
    }

    public UserLR updateStudent(Long id, UserLR updatedStudent) {
        Optional<UserLR> studentOptional = userLrRepository.findByIdAndAlumnoRole(id);

        if (studentOptional.isPresent()) {
            UserLR student = studentOptional.get();
            student.setName(updatedStudent.getName());
            student.setLast_name(updatedStudent.getLast_name());
            student.setBirthday(updatedStudent.getBirthday());
            student.setEmail(updatedStudent.getEmail());
            String encodedPassword = passwordEncoder.encode(updatedStudent.getPassword());
            student.setPassword(encodedPassword);
            return userLrRepository.save(student);
        } else {
            return null;
        }
    }

    public Optional<UserLR> findStudentById(Long id) {
        return userLrRepository.findByIdAndAlumnoRole(id);
    }

    public void deleteStudentById(Long id) {
        Optional<UserLR> studentOptional = userLrRepository.findByIdAndAlumnoRole(id);

        if (studentOptional.isPresent()) {
            UserLR student = studentOptional.get();
            userLrRepository.delete(student);
        } else {
            System.out.println("No se encontro el alumno");
        }
    }

}
