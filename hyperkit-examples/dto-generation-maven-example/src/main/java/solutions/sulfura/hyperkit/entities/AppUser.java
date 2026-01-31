package solutions.sulfura.hyperkit.entities;

import solutions.sulfura.hyperkit.dtos.annotations.Dto;
import solutions.sulfura.hyperkit.dtos.annotations.DtoProperty;

import java.util.Set;

@Dto(include = DtoProperty.class)
public class AppUser {

    @DtoProperty
    public Long id;
    @DtoProperty
    public String username;
    /*
    The @Dto annotation in this class is set to include only properties annotated with @DtoProperty;
    this property will not be included in the generated Dto
    */
    public String email;
    @DtoProperty
    public Set<AppPost> posts;

}