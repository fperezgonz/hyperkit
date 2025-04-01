package solutions.sulfura.hyperkit.dtos;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.SourceUpperCaseProperties;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import io.vavr.control.Option;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;

@DtoFor(SourceUpperCaseProperties.class)
public class SourceUpperCasePropertiesDto implements Dto<SourceUpperCaseProperties>{

    public Option<String> UPPERCASE_FIELD_PROPERTY = Option.none();
    public Option<String> PRESERVE_UPPERCASE_GETTER_PROPERTY = Option.none();
    public Option<String> uPPERCASE_GETTER_PROPERTY = Option.none();

    public SourceUpperCasePropertiesDto() {
    }

    public Class<SourceUpperCaseProperties> getSourceClass() {
        return SourceUpperCaseProperties.class;
    }

    public static class Builder{

        Option<String> UPPERCASE_FIELD_PROPERTY = Option.none();
        Option<String> PRESERVE_UPPERCASE_GETTER_PROPERTY = Option.none();
        Option<String> uPPERCASE_GETTER_PROPERTY = Option.none();

        public static  Builder newInstance(){
            return new Builder();
        }

        public Builder UPPERCASE_FIELD_PROPERTY(final Option<String> UPPERCASE_FIELD_PROPERTY){
            this.UPPERCASE_FIELD_PROPERTY = UPPERCASE_FIELD_PROPERTY == null ? Option.none() : UPPERCASE_FIELD_PROPERTY;
            return this;
        }

        public Builder PRESERVE_UPPERCASE_GETTER_PROPERTY(final Option<String> PRESERVE_UPPERCASE_GETTER_PROPERTY){
            this.PRESERVE_UPPERCASE_GETTER_PROPERTY = PRESERVE_UPPERCASE_GETTER_PROPERTY == null ? Option.none() : PRESERVE_UPPERCASE_GETTER_PROPERTY;
            return this;
        }

        public Builder uPPERCASE_GETTER_PROPERTY(final Option<String> uPPERCASE_GETTER_PROPERTY){
            this.uPPERCASE_GETTER_PROPERTY = uPPERCASE_GETTER_PROPERTY == null ? Option.none() : uPPERCASE_GETTER_PROPERTY;
            return this;
        }


        public SourceUpperCasePropertiesDto build(){

            SourceUpperCasePropertiesDto instance = new SourceUpperCasePropertiesDto();
            instance.UPPERCASE_FIELD_PROPERTY = UPPERCASE_FIELD_PROPERTY;
            instance.PRESERVE_UPPERCASE_GETTER_PROPERTY = PRESERVE_UPPERCASE_GETTER_PROPERTY;
            instance.uPPERCASE_GETTER_PROPERTY = uPPERCASE_GETTER_PROPERTY;

            return instance;

        }

    }

    @ProjectionFor(SourceUpperCasePropertiesDto.class)
    public static class Projection extends DtoProjection<SourceUpperCasePropertiesDto> {

        public FieldConf UPPERCASE_FIELD_PROPERTY;
        public FieldConf PRESERVE_UPPERCASE_GETTER_PROPERTY;
        public FieldConf uPPERCASE_GETTER_PROPERTY;

        public Projection() {
        }

        public void applyProjectionTo(SourceUpperCasePropertiesDto dto) throws DtoProjectionException {
            dto.UPPERCASE_FIELD_PROPERTY = ProjectionUtils.getProjectedValue(dto.UPPERCASE_FIELD_PROPERTY, this.UPPERCASE_FIELD_PROPERTY);
            dto.PRESERVE_UPPERCASE_GETTER_PROPERTY = ProjectionUtils.getProjectedValue(dto.PRESERVE_UPPERCASE_GETTER_PROPERTY, this.PRESERVE_UPPERCASE_GETTER_PROPERTY);
            dto.uPPERCASE_GETTER_PROPERTY = ProjectionUtils.getProjectedValue(dto.uPPERCASE_GETTER_PROPERTY, this.uPPERCASE_GETTER_PROPERTY);
        }

        public static class Builder{

            FieldConf UPPERCASE_FIELD_PROPERTY;
            FieldConf PRESERVE_UPPERCASE_GETTER_PROPERTY;
            FieldConf uPPERCASE_GETTER_PROPERTY;

            public static  Builder newInstance(){
                return new Builder();
            }

            public Builder UPPERCASE_FIELD_PROPERTY(final FieldConf UPPERCASE_FIELD_PROPERTY){
                this.UPPERCASE_FIELD_PROPERTY = UPPERCASE_FIELD_PROPERTY;
                return this;
            }

            public Builder UPPERCASE_FIELD_PROPERTY(final Presence presence){
                UPPERCASE_FIELD_PROPERTY = FieldConf.of(presence);
                return this;
            }

            public Builder PRESERVE_UPPERCASE_GETTER_PROPERTY(final FieldConf PRESERVE_UPPERCASE_GETTER_PROPERTY){
                this.PRESERVE_UPPERCASE_GETTER_PROPERTY = PRESERVE_UPPERCASE_GETTER_PROPERTY;
                return this;
            }

            public Builder PRESERVE_UPPERCASE_GETTER_PROPERTY(final Presence presence){
                PRESERVE_UPPERCASE_GETTER_PROPERTY = FieldConf.of(presence);
                return this;
            }

            public Builder uPPERCASE_GETTER_PROPERTY(final FieldConf uPPERCASE_GETTER_PROPERTY){
                this.uPPERCASE_GETTER_PROPERTY = uPPERCASE_GETTER_PROPERTY;
                return this;
            }

            public Builder uPPERCASE_GETTER_PROPERTY(final Presence presence){
                uPPERCASE_GETTER_PROPERTY = FieldConf.of(presence);
                return this;
            }

            public SourceUpperCasePropertiesDto.Projection build(){

                SourceUpperCasePropertiesDto.Projection instance = new SourceUpperCasePropertiesDto.Projection();
                instance.UPPERCASE_FIELD_PROPERTY = UPPERCASE_FIELD_PROPERTY;
                instance.PRESERVE_UPPERCASE_GETTER_PROPERTY = PRESERVE_UPPERCASE_GETTER_PROPERTY;
                instance.uPPERCASE_GETTER_PROPERTY = uPPERCASE_GETTER_PROPERTY;

                return instance;

            }

        }

    }

    public static class DtoModel{

        public static final String _UPPERCASE_FIELD_PROPERTY = "UPPERCASE_FIELD_PROPERTY";
        public static final String _PRESERVE_UPPERCASE_GETTER_PROPERTY = "PRESERVE_UPPERCASE_GETTER_PROPERTY";
        public static final String _uPPERCASE_GETTER_PROPERTY = "uPPERCASE_GETTER_PROPERTY";

    }

}