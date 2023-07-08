package solutions.sulfura.gend.dtos;

import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.SourceClassGetterSetter;
import java.lang.String;

@DtoFor(SourceClassGetterSetter.class)
public class SourceClassGetterSetterDto implements Dto<SourceClassGetterSetter>{

    public Option<String> stringPropertyWithGetter;
    public Option<String> stringPropertyWithSetter;
    public Option<String> stringPropertyWithGetterAndSetter;

    public SourceClassGetterSetterDto(){}

    public static  Builder builder(){
        return new Builder();
    }

    public static class Builder{

        public Option<String> stringPropertyWithGetter;
        public Option<String> stringPropertyWithSetter;
        public Option<String> stringPropertyWithGetterAndSetter;

        public Builder stringPropertyWithGetter(Option<String> stringPropertyWithGetter){
            this.stringPropertyWithGetter = stringPropertyWithGetter;
            return this;
        }

        public Builder stringPropertyWithSetter(Option<String> stringPropertyWithSetter){
            this.stringPropertyWithSetter = stringPropertyWithSetter;
            return this;
        }

        public Builder stringPropertyWithGetterAndSetter(Option<String> stringPropertyWithGetterAndSetter){
            this.stringPropertyWithGetterAndSetter = stringPropertyWithGetterAndSetter;
            return this;
        }

        public SourceClassGetterSetterDto build(){
            SourceClassGetterSetterDto instance = new SourceClassGetterSetterDto();
            instance.stringPropertyWithGetter = stringPropertyWithGetter;
            instance.stringPropertyWithSetter = stringPropertyWithSetter;
            instance.stringPropertyWithGetterAndSetter = stringPropertyWithGetterAndSetter;
            return instance;
        }

    }

}