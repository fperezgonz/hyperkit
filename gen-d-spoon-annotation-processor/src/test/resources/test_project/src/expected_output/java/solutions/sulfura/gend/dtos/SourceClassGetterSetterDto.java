package solutions.sulfura.gend.dtos;

import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.projection.ProjectionFor;
import solutions.sulfura.gend.dtos.projection.ProjectionUtils;
import solutions.sulfura.gend.dtos.projection.DtoProjectionException;
import solutions.sulfura.gend.dtos.projection.DtoProjection;
import solutions.sulfura.gend.dtos.projection.fields.FieldConf.Presence;
import solutions.sulfura.gend.dtos.projection.fields.FieldConf;
import java.lang.String;
import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.SourceClassGetterSetter;

@DtoFor(SourceClassGetterSetter.class)
public class SourceClassGetterSetterDto implements Dto<SourceClassGetterSetter>{

    public Option<String> stringPropertyWithGetter = Option.none();
    public Option<String> stringPropertyWithSetter = Option.none();
    public Option<String> stringPropertyWithGetterAndSetter = Option.none();

    public SourceClassGetterSetterDto(){}

    public Class<SourceClassGetterSetter> getSourceClass() {
        return SourceClassGetterSetter.class;
    }

    public static class Builder{

        public Option<String> stringPropertyWithGetter = Option.none();
        public Option<String> stringPropertyWithSetter = Option.none();
        public Option<String> stringPropertyWithGetterAndSetter = Option.none();

        public static  Builder newInstance(){
            return new Builder();
        }

        public Builder stringPropertyWithGetter(Option<String> stringPropertyWithGetter){
            this.stringPropertyWithGetter = stringPropertyWithGetter == null ? Option.none() : stringPropertyWithGetter;
            return this;
        }

        public Builder stringPropertyWithSetter(Option<String> stringPropertyWithSetter){
            this.stringPropertyWithSetter = stringPropertyWithSetter == null ? Option.none() : stringPropertyWithSetter;
            return this;
        }

        public Builder stringPropertyWithGetterAndSetter(Option<String> stringPropertyWithGetterAndSetter){
            this.stringPropertyWithGetterAndSetter = stringPropertyWithGetterAndSetter == null ? Option.none() : stringPropertyWithGetterAndSetter;
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

    @ProjectionFor(SourceClassGetterSetterDto.class)
    public static class Projection extends DtoProjection<SourceClassGetterSetterDto>{

        public FieldConf stringPropertyWithGetter;
        public FieldConf stringPropertyWithSetter;
        public FieldConf stringPropertyWithGetterAndSetter;

        public void applyProjectionTo(SourceClassGetterSetterDto dto) throws DtoProjectionException {
            dto.stringPropertyWithGetter = ProjectionUtils.getProjectedValue(dto.stringPropertyWithGetter, this.stringPropertyWithGetter);
            dto.stringPropertyWithSetter = ProjectionUtils.getProjectedValue(dto.stringPropertyWithSetter, this.stringPropertyWithSetter);
            dto.stringPropertyWithGetterAndSetter = ProjectionUtils.getProjectedValue(dto.stringPropertyWithGetterAndSetter, this.stringPropertyWithGetterAndSetter);
        }

        public Projection(){}

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

            public Builder stringPropertyWithGetter(Presence presence){
                stringPropertyWithGetter = FieldConf.of(presence);
                return this;
            }

            public Builder stringPropertyWithSetter(FieldConf stringPropertyWithSetter){
                this.stringPropertyWithSetter = stringPropertyWithSetter;
                return this;
            }

            public Builder stringPropertyWithSetter(Presence presence){
                stringPropertyWithSetter = FieldConf.of(presence);
                return this;
            }

            public Builder stringPropertyWithGetterAndSetter(FieldConf stringPropertyWithGetterAndSetter){
                this.stringPropertyWithGetterAndSetter = stringPropertyWithGetterAndSetter;
                return this;
            }

            public Builder stringPropertyWithGetterAndSetter(Presence presence){
                stringPropertyWithGetterAndSetter = FieldConf.of(presence);
                return this;
            }

            public SourceClassGetterSetterDto.Projection build(){
                SourceClassGetterSetterDto.Projection instance = new SourceClassGetterSetterDto.Projection();
                instance.stringPropertyWithGetter = stringPropertyWithGetter;
                instance.stringPropertyWithSetter = stringPropertyWithSetter;
                instance.stringPropertyWithGetterAndSetter = stringPropertyWithGetterAndSetter;
                return instance;
            }

        }


    }

    public static class DtoModel{

        public static final String _stringPropertyWithGetter = "stringPropertyWithGetter";
        public static final String _stringPropertyWithSetter = "stringPropertyWithSetter";
        public static final String _stringPropertyWithGetterAndSetter = "stringPropertyWithGetterAndSetter";

    }

}