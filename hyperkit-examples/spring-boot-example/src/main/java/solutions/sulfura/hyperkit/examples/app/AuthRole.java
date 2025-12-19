package solutions.sulfura.hyperkit.examples.app;

import solutions.sulfura.hyperkit.dtos.annotations.Dto;import java.util.Set;

@Dto
public class AuthRole {
    public Long id;
    public String name;
    public Set<Permission> permissions;

    @Dto
    public static class Permission {
        public Long id;
        public String name;
    }
}
