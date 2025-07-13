package solutions.sulfura.hyperkit.dsl.projections;

import solutions.sulfura.hyperkit.dtos.Dto;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DtoProjectionSpec {

    String namespace() default "";

    /**
     * The class that is projected by the root of the dsl string in {@link #value}
     */
    @SuppressWarnings("rawtypes")
    Class<? extends Dto> projectedClass();

    /**
     * A valid projections dsl string
     */
    String value();

}
