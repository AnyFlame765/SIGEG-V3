package utez.edu.mx.smartfitutez.security.entity;

import utez.edu.mx.smartfitutez.security.enums.RoleName;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    public Role() {
        
    }

    public Role(RoleName roleName) {
        this.roleName = roleName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RoleName getRoleName() {
        return roleName;
    }

    public void setRoleName(RoleName roleName) {
        this.roleName = roleName;
    }


}
