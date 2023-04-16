package utez.edu.mx.smartfitutez.security.entity;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.lang.Nullable;
import utez.edu.mx.smartfitutez.models.record.Record;
import utez.edu.mx.smartfitutez.models.routines.Routines;
import utez.edu.mx.smartfitutez.security.dto.NewUser;
import java.util.*;


@Entity
@Table(name = "usersLR",
        uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class UserLR {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nullable
    @Size(max = 50)
    private String name;

    @Nullable
    @Size(max = 50)
    private String last_name;

    @Temporal(TemporalType.DATE)
    private Date birthday;

    @Nullable
    private double current_weight;

    @Nullable
    private String height;

    @Nullable
    private String gender;

    @Nullable
    @Size(max = 50)
    @Email
    private String email;

    @Nullable
    @Size(max = 120)
    private String password;

    @OneToMany(mappedBy = "userLR", cascade = CascadeType.ALL)
    private List<Routines> routines = new ArrayList<>();

    @OneToMany(mappedBy = "userLR", cascade = CascadeType.ALL)
    private List<Record> records = new ArrayList<>();

    @Nullable
    @ManyToMany
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public UserLR() {
    }

    public UserLR(String name, String last_name, Date birthday, double current_weight, String height, String gender, String email, String password) {
        this.name = name;
        this.last_name = last_name;
        this.birthday = birthday;
        this.current_weight = current_weight;
        this.height = height;
        this.gender = gender;
        this.email = email;
        this.password = password;
    }

    public UserLR(String name, String last_name, Date birthday, double current_weight, String height, String gender, String email, Set<NewUser> roles, String encode) {
    }


    //Instructor
    public UserLR(String name, String last_name, Date birthday, String email, String password) {
        this.name = name;
        this.last_name = last_name;
        this.birthday = birthday;
        this.email = email;
        this.password = password;
    }

    public UserLR(String name, String last_name, Date birthday, String email, String password, String encode) {
    }

    //Constructor de admin
    public UserLR(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public UserLR(String name, String email, String password,String encode){

    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public double getCurrent_weight() {
        return current_weight;
    }

    public void setCurrent_weight(double current_weight) {
        this.current_weight = current_weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public UserLR get() {
        return this;
    }
}
