package solutions.sulfura.gend.dtos;

import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.conf.DtoConf;
import solutions.sulfura.gend.dtos.conf.fields.FieldConf;
import java.lang.String;
import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.SourceClassGetterSetter;

@DtoFor(SourceClassGetterSetter.class)
public class SourceClassGetterSetterDto implements Dto<SourceClassGetterSetter>{

    public Option<String> stringPropertyWithGetter = Option.some(null);
    public Option<String> stringPropertyWithSetter = Option.some(null);
    public Option<String> stringPropertyWithGetterAndSetter = Option.some(null);

    public SourceClassGetterSetterDto(){}

    public static class Builder{

        public Option<String> stringPropertyWithGetter = Option.some(null);
        public Option<String> stringPropertyWithSetter = Option.some(null);
        public Option<String> stringPropertyWithGetterAndSetter = Option.some(null);

        public static  Builder newInstance(){
            return new Builder();
        }

        public Builder stringPropertyWithGetter(Option<String> stringPropertyWithGetter){
            this.stringPropertyWithGetter = stringPropertyWithGetter == null ? Option.some(null) : stringPropertyWithGetter;
            return this;
        }

        public Builder stringPropertyWithSetter(Option<String> stringPropertyWithSetter){
            this.stringPropertyWithSetter = stringPropertyWithSetter == null ? Option.some(null) : stringPropertyWithSetter;
            return this;
        }

        public Builder stringPropertyWithGetterAndSetter(Option<String> stringPropertyWithGetterAndSetter){
            this.stringPropertyWithGetterAndSetter = stringPropertyWithGetterAndSetter == null ? Option.some(null) : stringPropertyWithGetterAndSetter;
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

    public static class Conf extends DtoConf<SourceClassGetterSetterDto>{

        public FieldConf stringPropertyWithGetter;
        public FieldConf stringPropertyWithSetter;
        public FieldConf stringPropertyWithGetterAndSetter;

        public Conf(){}

        public static class Builder{

            public FieldConf stringPropertyWithGetter;
            public FieldConf stringPropertyWithSetter;
            public FieldConf stringPropertyWithGetterAndSetter;

            public static  Builder newInstance(){
                return new Builder();
            }

            public Builder stringPropertyWithGetter(FieldConf stringPropertyWithGetter){
                this.stringPropertyWithGetter = stringPropertyWithGetter;
                return this;
            }

            public Builder stringPropertyWithSetter(FieldConf stringPropertyWithSetter){
                this.stringPropertyWithSetter = stringPropertyWithSetter;
                return this;
            }

            public Builder stringPropertyWithGetterAndSetter(FieldConf stringPropertyWithGetterAndSetter){
                this.stringPropertyWithGetterAndSetter = stringPropertyWithGetterAndSetter;
                return this;
            }

            public SourceClassGetterSetterDto.Conf build(){
                SourceClassGetterSetterDto.Conf instance = new SourceClassGetterSetterDto.Conf();
                instance.stringPropertyWithGetter = stringPropertyWithGetter;
                instance.stringPropertyWithSetter = stringPropertyWithSetter;
                instance.stringPropertyWithGetterAndSetter = stringPropertyWithGetterAndSetter;
                return instance;
            }

        }


    }

}