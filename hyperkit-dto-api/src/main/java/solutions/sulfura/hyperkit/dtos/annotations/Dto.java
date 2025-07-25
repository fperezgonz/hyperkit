package solutions.sulfura.hyperkit.dtos.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Dto {

    Class<? extends Annotation>[] include() default {};

    String destPackageName() default "";

}