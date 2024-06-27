package solutions.sulfura.gend.dtos;

import solutions.sulfura.gend.dtos.annotations.Dto;

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
