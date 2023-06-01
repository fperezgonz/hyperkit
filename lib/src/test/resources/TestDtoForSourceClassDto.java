package solutions.sulfura.gend.dtos;

import java.util.List;

import io.vavr.control.Option;

/** This class is used as input for the DTO generator test */
@DtoFor(TestDtoSourceClass.class)
public class TestDtoForSourceClassDto {

    public Option<String> stringPropertyWithGetter;
    public Option<String> stringPropertyWithoutGetter;
    public Option<String> stringPropertyWithAnnotationOnGetter;
    public Option<String> stringPropertyWithAnnotationOverlappedWithGetter;
    public Option<Long> longProperty;
    public Option<Boolean> booleanProperty;
    public Option<Double> doubleProperty;
    public Option<List<ListOperation<String>>> stringArrayProperty;
    public Option<List<ListOperation<Boolean>>> booleanArrayProperty;

}
