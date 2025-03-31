package solutions.sulfura.hyperkit.dtos.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface DtoProperty {
    String propertyName() default "";
    boolean createGetter() default false;
    boolean createSetter() default false;
    boolean preserveCase() default false;
}