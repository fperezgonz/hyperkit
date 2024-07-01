package solutions.sulfura.gend.dtos.projection;

import solutions.sulfura.gend.dtos.Dto;

/*TODO allow configuring with annotations instead.
 * <br>
 * <br>
 * At the time of this writing, I could not find a way to make this work with type safety and annotations and relative ease of use,
 * mostly due to the lack of support for generics in annotations
 * The main problem would be that to define a config annotation for a specific Dto, each field Type would need its own annotation, which would make the framework hard to use.
 * <br>
 * <br>
 * Example of the problem:
 * <pre>{@code
 * @FooDtoConfig {
 *     IntFieldConf intField = @IntFieldConf;
 *     FloatFieldConf floatField = @FloatFieldConf;
 *     FooFieldConf fooFieldConf = @FooFieldConf;
 *     FooArrayFieldConf fooArrayConf = @FooArrayFieldConf;
 * }
 * }</pre>
 * */
public abstract class DtoProjection<T extends Dto<?>> {

}