/*package utez.edu.mx.smartfitutez.security.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import utez.edu.mx.smartfitutez.security.entity.Role;
import utez.edu.mx.smartfitutez.security.enums.RoleName;
import utez.edu.mx.smartfitutez.security.service.RoleService;

@Component
public class CreateRoles implements CommandLineRunner {
    @Autowired
    RoleService roleService;

    @Override
    public void run(String... args) throws Exception {
        Role roleAdmin = new Role(RoleName.ROLE_ADMIN);
        Role roleInstructor = new Role(RoleName.ROLE_INSTRUCTOR);
        Role roleUser = new Role(RoleName.ROLE_USER);
        roleService.save(roleAdmin);
        roleService.save(roleInstructor);
        roleService.save(roleUser);
    }
}*/

