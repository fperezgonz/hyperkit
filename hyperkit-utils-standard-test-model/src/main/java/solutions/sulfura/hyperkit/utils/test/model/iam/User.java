package solutions.sulfura.hyperkit.utils.test.model.iam;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;
import solutions.sulfura.hyperkit.utils.test.model.Account;

import java.util.Set;

@Entity
@Dto
public class User {
    @Id
    public String id;
    public String username;
    public String email;
    @ManyToMany
    public Set<Authorization> authorizations;
    @OneToMany(mappedBy = "user")
    public Set<Account> account;

}
