package solutions.sulfura.gend.dtos;

import solutions.sulfura.gend.dtos.SourceClassGetterSetter;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.projection.fields.FieldConf;
import solutions.sulfura.gend.dtos.projection.DtoProjectionException;
import solutions.sulfura.gend.dtos.projection.DtoProjection;
import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.projection.ProjectionFor;
import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.projection.ProjectionUtils;
import solutions.sulfura.gend.dtos.projection.fields.FieldConf.Presence;

@DtoFor(SourceClassGetterSetter.class)
public class SourceClassGetterSetterDto implements Dto<SourceClassGetterSetter>{

    public Option<String> stringPropertyWithGetter = Option.none();
    public Option<String> stringPropertyWithGetterAndSetter = Option.none();
    public Option<String> stringPropertyWithSetter = Option.none();

    public SourceClassGetterSetterDto() {
    }

    public Class<SourceClassGetterSetter> getSourceClass() {
        return SourceClassGetterSetter.class;
    }

    public static class Builder{

        Option<String> stringPropertyWithGetter = Option.none();
        Option<String> stringPropertyWithGetterAndSetter = Option.none();
        Option<String> stringPropertyWithSetter = Option.none();

        public static  Builder newInstance(){
            return new Builder();
        }

        public Builder stringPropertyWithGetter(final Option<String> stringPropertyWithGetter){
            this.stringPropertyWithGetter = stringPropertyWithGetter == null ? Option.none() : stringPropertyWithGetter;
            return this;
        }

        public Builder stringPropertyWithGetterAndSetter(final Option<String> stringPropertyWithGetterAndSetter){
            this.stringPropertyWithGetterAndSetter = stringPropertyWithGetterAndSetter == null ? Option.none() : stringPropertyWithGetterAndSetter;
            return this;
        }

        public Builder stringPropertyWithSetter(final Option<String> stringPropertyWithSetter){
            this.stringPropertyWithSetter = stringPropertyWithSetter == null ? Option.none() : stringPropertyWithSetter;
            return this;
        }


        public SourceClassGetterSetterDto build(){

            SourceClassGetterSetterDto instance = new SourceClassGetterSetterDto();
            instance.stringPropertyWithGetter = stringPropertyWithGetter;
            instance.stringPropertyWithGetterAndSetter = stringPropertyWithGetterAndSetter;
            instance.stringPropertyWithSetter = stringPropertyWithSetter;

            return instance;

        }

    }

    @ProjectionFor(SourceClassGetterSetterDto.class)
    public static class Projection extends DtoProjection<SourceClassGetterSetterDto> {

        public FieldConf stringPropertyWithGetter;
        public FieldConf stringPropertyWithGetterAndSetter;
        public FieldConf stringPropertyWithSetter;

        public Projection() {
        }

        public void applyProjectionTo(SourceClassGetterSetterDto dto) throws DtoProjectionException {
            dto.stringPropertyWithGetter = ProjectionUtils.getProjectedValue(dto.stringPropertyWithGetter, this.stringPropertyWithGetter);
            dto.stringPropertyWithGetterAndSetter = ProjectionUtils.getProjectedValue(dto.stringPropertyWithGetterAndSetter, this.stringPropertyWithGetterAndSetter);
            dto.stringPropertyWithSetter = ProjectionUtils.getProjectedValue(dto.stringPropertyWithSetter, this.stringPropertyWithSetter);
        }

        public static class Builder{

            FieldConf stringPropertyWithGetter;
            FieldConf stringPropertyWithGetterAndSetter;
            FieldConf stringPropertyWithSetter;

            public static  Builder newInstance(){
                return new Builder();
            }

            public Builder stringPropertyWithGetter(final FieldConf stringPropertyWithGetter){
                this.stringPropertyWithGetter = stringPropertyWithGetter;
                return this;
            }

            public Builder stringPropertyWithGetter(final Presence presence){
                stringPropertyWithGetter = FieldConf.of(presence);
                return this;
            }

            public Builder stringPropertyWithGetterAndSetter(final FieldConf stringPropertyWithGetterAndSetter){
                this.stringPropertyWithGetterAndSetter = stringPropertyWithGetterAndSetter;
                return this;
            }

            public Builder stringPropertyWithGetterAndSetter(final Presence presence){
                stringPropertyWithGetterAndSetter = FieldConf.of(presence);
                return this;
            }

            public Builder stringPropertyWithSetter(final FieldConf stringPropertyWithSetter){
                this.stringPropertyWithSetter = stringPropertyWithSetter;
                return this;
            }

            public Builder stringPropertyWithSetter(final Presence presence){
                stringPropertyWithSetter = FieldConf.of(presence);
                return this;
            }

            public SourceClassGetterSetterDto.Projection build(){

                SourceClassGetterSetterDto.Projection instance = new SourceClassGetterSetterDto.Projection();
                instance.stringPropertyWithGetter = stringPropertyWithGetter;
                instance.stringPropertyWithGetterAndSetter = stringPropertyWithGetterAndSetter;
                instance.stringPropertyWithSetter = stringPropertyWithSetter;

                return instance;

            }

        }

    }

    public static class DtoModel{

        public static final String _stringPropertyWithGetter = "stringPropertyWithGetter";
        public static final String _stringPropertyWithGetterAndSetter = "stringPropertyWithGetterAndSetter";
        public static final String _stringPropertyWithSetter = "stringPropertyWithSetter";

    }

}