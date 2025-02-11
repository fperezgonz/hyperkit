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
import solutions.sulfura.gend.dtos.SourceUpperCaseProperties;

@DtoFor(SourceUpperCaseProperties.class)
public class SourceUpperCasePropertiesDto implements Dto<SourceUpperCaseProperties>{

    public Option<String> UPPERCASE_FIELD_PROPERTY = Option.none();
    public Option<String> UPPERCASE_GETTER_PROPERTY = Option.none();

    public SourceUpperCasePropertiesDto(){}

    public Class<SourceUpperCaseProperties> getSourceClass() {
        return SourceUpperCaseProperties.class;
    }

    public static class Builder{

        public Option<String> UPPERCASE_FIELD_PROPERTY = Option.none();
        public Option<String> UPPERCASE_GETTER_PROPERTY = Option.none();

        public static  Builder newInstance(){
            return new Builder();
        }

        public Builder UPPERCASE_FIELD_PROPERTY(Option<String> UPPERCASE_FIELD_PROPERTY){
            this.UPPERCASE_FIELD_PROPERTY = UPPERCASE_FIELD_PROPERTY == null ? Option.none() : UPPERCASE_FIELD_PROPERTY;
            return this;
        }

        public Builder UPPERCASE_GETTER_PROPERTY(Option<String> UPPERCASE_GETTER_PROPERTY){
            this.UPPERCASE_GETTER_PROPERTY = UPPERCASE_GETTER_PROPERTY == null ? Option.none() : UPPERCASE_GETTER_PROPERTY;
            return this;
        }

        public SourceUpperCasePropertiesDto build(){
            SourceUpperCasePropertiesDto instance = new SourceUpperCasePropertiesDto();
            instance.UPPERCASE_FIELD_PROPERTY = UPPERCASE_FIELD_PROPERTY;
            instance.UPPERCASE_GETTER_PROPERTY = UPPERCASE_GETTER_PROPERTY;
            return instance;
        }

    }

    @ProjectionFor(SourceUpperCasePropertiesDto.class)
    public static class Projection extends DtoProjection<SourceUpperCasePropertiesDto>{

        public FieldConf UPPERCASE_FIELD_PROPERTY;
        public FieldConf UPPERCASE_GETTER_PROPERTY;

        public void applyProjectionTo(SourceUpperCasePropertiesDto dto) throws DtoProjectionException {
            dto.UPPERCASE_FIELD_PROPERTY = ProjectionUtils.getProjectedValue(dto.UPPERCASE_FIELD_PROPERTY, this.UPPERCASE_FIELD_PROPERTY);
            dto.UPPERCASE_GETTER_PROPERTY = ProjectionUtils.getProjectedValue(dto.UPPERCASE_GETTER_PROPERTY, this.UPPERCASE_GETTER_PROPERTY);
        }

        public Projection(){}

        public static class Builder{

            public FieldConf UPPERCASE_FIELD_PROPERTY;
            public FieldConf UPPERCASE_GETTER_PROPERTY;

            public static  Builder newInstance(){
                return new Builder();
            }

            public Builder UPPERCASE_FIELD_PROPERTY(FieldConf UPPERCASE_FIELD_PROPERTY){
                this.UPPERCASE_FIELD_PROPERTY = UPPERCASE_FIELD_PROPERTY;
                return this;
            }

            public Builder UPPERCASE_FIELD_PROPERTY(Presence presence){
                UPPERCASE_FIELD_PROPERTY = FieldConf.of(presence);
                return this;
            }

            public Builder UPPERCASE_GETTER_PROPERTY(FieldConf UPPERCASE_GETTER_PROPERTY){
                this.UPPERCASE_GETTER_PROPERTY = UPPERCASE_GETTER_PROPERTY;
                return this;
            }

            public Builder UPPERCASE_GETTER_PROPERTY(Presence presence){
                UPPERCASE_GETTER_PROPERTY = FieldConf.of(presence);
                return this;
            }

            public SourceUpperCasePropertiesDto.Projection build(){
                SourceUpperCasePropertiesDto.Projection instance = new SourceUpperCasePropertiesDto.Projection();
                instance.UPPERCASE_FIELD_PROPERTY = UPPERCASE_FIELD_PROPERTY;
                instance.UPPERCASE_GETTER_PROPERTY = UPPERCASE_GETTER_PROPERTY;
                return instance;
            }

        }


    }

    public static class DtoModel{

        public static final String _UPPERCASE_FIELD_PROPERTY = "UPPERCASE_FIELD_PROPERTY";
        public static final String _UPPERCASE_GETTER_PROPERTY = "UPPERCASE_GETTER_PROPERTY";

    }

}