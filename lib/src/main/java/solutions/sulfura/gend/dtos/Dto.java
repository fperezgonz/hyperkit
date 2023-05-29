package solutions.sulfura.gend.dtos;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Dto {

    Class<?extends Annotation>[] include() default {};

    String destPackageName() default "";

    String prefix() default "";

    String suffix() default "Dto";
}