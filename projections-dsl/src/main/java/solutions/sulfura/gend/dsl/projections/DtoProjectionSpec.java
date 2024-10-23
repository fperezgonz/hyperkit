package solutions.sulfura.gend.dsl.projections;

import solutions.sulfura.gend.dtos.Dto;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DtoProjectionSpec {

    /**
     * The class that is projected by the root of the dsl string in {@link #value}
     */
    Class<? extends Dto> projectedClass();

    /**
     * A valid projections dsl string
     */
    String value();

}
