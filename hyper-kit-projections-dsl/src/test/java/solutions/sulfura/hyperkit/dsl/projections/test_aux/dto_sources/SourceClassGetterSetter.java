package solutions.sulfura.hyperkit.dsl.projections.test_aux.dto_sources;

import solutions.sulfura.hyperkit.dtos.annotations.Dto;

/**This class is used as input for the DTO generator test */
@Dto
public class SourceClassGetterSetter {

    String stringPropertyWithGetter;
    String stringPropertyWithGetterAndSetter;
    String stringPropertyWithSetter;

    public String getStringPropertyWithGetter(){ return stringPropertyWithGetter; }

    public String getStringPropertyWithGetterAndSetter(){ return stringPropertyWithGetterAndSetter; }

    public void setStringPropertyWithGetterAndSetter(String stringPropertyWithGetterAndSetter){ this.stringPropertyWithGetterAndSetter = stringPropertyWithGetterAndSetter; }

    public void setStringPropertyWithSetter(String stringPropertyWithSetter){ this.stringPropertyWithSetter = stringPropertyWithSetter; }

}
