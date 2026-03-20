package solutions.sulfura.hyperkit.dtos;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import java.util.Date;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.SourceClassReferencesClassesWithSameName;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import java.util.Objects;

@DtoFor(SourceClassReferencesClassesWithSameName.class)
public class SourceClassReferencesClassesWithSameNameDto implements Dto<SourceClassReferencesClassesWithSameName> {

    public ValueWrapper<Date> dateProperty = ValueWrapper.empty();
    public ValueWrapper<java.sql.Date> sqlDateProperty = ValueWrapper.empty();

    public SourceClassReferencesClassesWithSameNameDto() {
    }

    public Class<SourceClassReferencesClassesWithSameName> getSourceClass() {
        return SourceClassReferencesClassesWithSameName.class;
    }

    public static class Builder {

        ValueWrapper<Date> dateProperty = ValueWrapper.empty();
        ValueWrapper<java.sql.Date> sqlDateProperty = ValueWrapper.empty();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder dateProperty(final ValueWrapper<Date> dateProperty){
            this.dateProperty = dateProperty == null ? ValueWrapper.empty() : dateProperty;
            return this;
        }

        public Builder sqlDateProperty(final ValueWrapper<java.sql.Date> sqlDateProperty){
            this.sqlDateProperty = sqlDateProperty == null ? ValueWrapper.empty() : sqlDateProperty;
            return this;
        }


        public SourceClassReferencesClassesWithSameNameDto build() {

            SourceClassReferencesClassesWithSameNameDto instance = new SourceClassReferencesClassesWithSameNameDto();
            instance.dateProperty = dateProperty;
            instance.sqlDateProperty = sqlDateProperty;

            return instance;

        }

    }

    @ProjectionFor(SourceClassReferencesClassesWithSameNameDto.class)
    public static class Projection extends DtoProjection<SourceClassReferencesClassesWithSameNameDto> {

        public FieldConf dateProperty;
        public FieldConf sqlDateProperty;

        public Projection() {
        }

        public void applyProjectionTo(SourceClassReferencesClassesWithSameNameDto dto) throws DtoProjectionException {
            dto.dateProperty = ProjectionUtils.getProjectedValue(dto.dateProperty, this.dateProperty);
            dto.sqlDateProperty = ProjectionUtils.getProjectedValue(dto.sqlDateProperty, this.sqlDateProperty);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return  Objects.equals(dateProperty, that.dateProperty)
                       && Objects.equals(sqlDateProperty, that.sqlDateProperty);

        }

        @Override
        public int hashCode() {
            return Objects.hash(dateProperty,
                    sqlDateProperty);
        }

        public static class Builder {

            FieldConf dateProperty;
            FieldConf sqlDateProperty;

            public static Builder newInstance() {
                return new Builder();
            }

            public Builder dateProperty(final FieldConf dateProperty){
                this.dateProperty = dateProperty;
                return this;
            }

            public Builder dateProperty(final Presence presence){
                dateProperty = FieldConf.of(presence);
                return this;
            }

            public Builder sqlDateProperty(final FieldConf sqlDateProperty){
                this.sqlDateProperty = sqlDateProperty;
                return this;
            }

            public Builder sqlDateProperty(final Presence presence){
                sqlDateProperty = FieldConf.of(presence);
                return this;
            }

            public SourceClassReferencesClassesWithSameNameDto.Projection build() {

                SourceClassReferencesClassesWithSameNameDto.Projection instance = new SourceClassReferencesClassesWithSameNameDto.Projection();
                instance.dateProperty = dateProperty;
                instance.sqlDateProperty = sqlDateProperty;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _dateProperty = "dateProperty";
        public static final String _sqlDateProperty = "sqlDateProperty";

    }

}