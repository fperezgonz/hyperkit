package solutions.sulfura.hyperkit.entities;

import solutions.sulfura.hyperkit.dtos.annotations.Dto;
import solutions.sulfura.hyperkit.dtos.annotations.DtoProperty;

@Dto
public class AppPost {

    @DtoProperty
    public Long id;
    @DtoProperty
    public String title;
    @DtoProperty
    public String content;
    @DtoProperty
    public Long authorId;
    @DtoProperty
    public AppUser appUser;

}