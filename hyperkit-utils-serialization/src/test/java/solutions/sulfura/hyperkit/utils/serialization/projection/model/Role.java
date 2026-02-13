package solutions.sulfura.hyperkit.utils.serialization.projection.model;

import solutions.sulfura.hyperkit.dtos.annotations.Dto;

import java.util.Set;

@Dto
public class Role {
    public String id;
    public String name;
    public Set<Action> actions;
}
