package utez.edu.mx.smartfitutez.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utez.edu.mx.smartfitutez.security.entity.Role;
import utez.edu.mx.smartfitutez.security.enums.RoleName;
import utez.edu.mx.smartfitutez.security.repository.RoleRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class RoleService {
    @Autowired
    RoleRepository roleRepository;
    public Optional<Role> getByRoleName(RoleName roleName){
        return roleRepository.findByRoleName(roleName);
    }
    public void save(Role role){
        roleRepository.save(role);
    }
}

