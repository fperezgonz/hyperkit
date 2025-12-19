package solutions.sulfura.hyperkit.examples.app;

import solutions.sulfura.hyperkit.dtos.annotations.Dto;

import java.util.Set;

@Dto
public class User {
    public Long id;
    public String name;
    public String email;
    public Set<AuthRole> roles;

}
