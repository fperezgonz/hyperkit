package solutions.sulfura.gend.dsl.projections;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DtoProjectionDef {

    String name() default "";

    String projectionDef();

}
