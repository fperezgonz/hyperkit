package solutions.sulfura.hyperkit.examples.model;

import jakarta.persistence.*;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;import java.util.Set;

@Dto
@Entity
@Table(name = "iam_auth_roles")
public class AuthRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    public String name;
    @OneToMany(mappedBy = "role")
    public Set<Permission> permissions;
    @ManyToOne
    public User user;

    @Dto
    @Entity
    @Table(name = "iam_permissions")
    public static class Permission {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        public Long id;
        public String name;
        @ManyToOne
        public AuthRole role;
    }
}
