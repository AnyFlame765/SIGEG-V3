package utez.edu.mx.smartfitutez.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import utez.edu.mx.smartfitutez.security.entity.Role;
import utez.edu.mx.smartfitutez.security.enums.RoleName;

import java.util.List;
import java.util.Optional;
@EnableJpaRepositories
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(RoleName roleName);
}

