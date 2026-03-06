package solutions.sulfura.hyperkit.utils.test.model.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;
import solutions.sulfura.hyperkit.utils.test.model.model.iam.User;

@Entity
@Dto
public class Account {
    @Id
    public String id;
    public String name;
    @ManyToOne
    public User user;
}
