/*package utez.edu.mx.smartfitutez.security.controller;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.smartfitutez.security.dto.Mensaje;
import utez.edu.mx.smartfitutez.security.entity.Role;
import utez.edu.mx.smartfitutez.security.entity.UserLR;
import utez.edu.mx.smartfitutez.security.enums.RoleName;
import utez.edu.mx.smartfitutez.security.service.UserLrService;

import java.util.Set;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {
    @Autowired
    UserLrService userLrService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        if (!userLrService.existsById(id))
            return new ResponseEntity<>(new Mensaje("No existe"), HttpStatus.NOT_FOUND);
        UserLR userLR = userLrService.getOne(id).get();
        Set<Role> roles = userLR.getRoles();
        for (Role role : roles) {
            if (role.getRoleName().equals(RoleName.ROLE_USER)) {
                userLrService.delete(id);
                return new ResponseEntity<>(new Mensaje("Usuario eliminado"), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new Mensaje("No se pude eliminar el Cliente"), HttpStatus.BAD_REQUEST);
    }


    //ROLES DE INSTRUCTOR
    //listar usuarios con rol de instructor (aun no cala)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/listInstructor")
    public ResponseEntity<?> listInstructor() {
       //listar usuarios con rol de instructor
        return new ResponseEntity<>(userLrService.listInstructor(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/deleteInstructor/{id}")
    public ResponseEntity<?> deleteInstructor(@PathVariable("id") Long id) {
        if (!userLrService.existsById(id))
            return new ResponseEntity<>(new Mensaje("No existe"), HttpStatus.NOT_FOUND);
        UserLR userLR = userLrService.getOne(id).get();
        Set<Role> roles = userLR.getRoles();
        for (Role role : roles) {
            if (role.getRoleName().equals(RoleName.ROLE_INSTRUCTOR)) {
                userLrService.delete(id);
                return new ResponseEntity<>(new Mensaje("Instructor eliminado"), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new Mensaje("No se pude eliminar el Instructor"), HttpStatus.BAD_REQUEST);
    }

    //Actualizar rol de instructor
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/updateInstructor/{id}")
    public ResponseEntity<?> updateInstructor(@PathVariable("id") Long id, @RequestBody UserLR userLR) {
        if (!userLrService.existsById(id))
            return new ResponseEntity<>(new Mensaje("No existe"), HttpStatus.NOT_FOUND);
        if (userLrService.existsByEmail(userLR.getEmail()))
            return new ResponseEntity<>(new Mensaje("ese email ya existe"), HttpStatus.BAD_REQUEST);
        UserLR userLR1 = userLrService.getOne(id).get();
        userLR1.setName(userLR.getName());
        userLR1.setLast_name(userLR.getLast_name());
        userLR1.setMiddle_name(userLR.getMiddle_name());
        userLR1.setEmail(userLR.getEmail());
        userLR1.setPassword(userLR.getPassword());
        userLrService.save(userLR1);
        return new ResponseEntity<>(new Mensaje("Instructor actualizado"), HttpStatus.OK);
    }
}
*/
