package solutions.sulfura.gend.dtos.circular_dependencies;

import solutions.sulfura.gend.dtos.annotations.Dto;

/**This class is used as input for the DTO generator test */
@Dto
public class SourceClassB {

    SourceClassB stringPropertyWithGetter;
    SourceClassB stringPropertyWithGetterAndSetter;
    SourceClassB stringPropertyWithSetter;

    public SourceClassB getStringPropertyWithGetter(){ return stringPropertyWithGetter; }

    public SourceClassB getStringPropertyWithGetterAndSetter(){ return stringPropertyWithGetterAndSetter; }

    public void setStringPropertyWithGetterAndSetter(SourceClassB stringPropertyWithGetterAndSetter){ this.stringPropertyWithGetterAndSetter = stringPropertyWithGetterAndSetter; }

    public void setStringPropertyWithSetter(SourceClassB stringPropertyWithSetter){ this.stringPropertyWithSetter = stringPropertyWithSetter; }

}
