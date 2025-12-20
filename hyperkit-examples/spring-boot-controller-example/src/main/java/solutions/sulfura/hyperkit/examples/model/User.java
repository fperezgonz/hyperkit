package solutions.sulfura.hyperkit.examples.model;

import jakarta.persistence.*;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;

import java.util.Set;

@Dto
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    public String name;
    public String email;
    @OneToMany(mappedBy = "user")
    public Set<AuthRole> roles;

}
