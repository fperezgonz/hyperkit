package solutions.sulfura.hyperkit.utils.test.model.dtos;

import solutions.sulfura.hyperkit.dtos.Dto;
import java.util.Set;
import solutions.sulfura.hyperkit.utils.test.model.dtos.DeliveryNoteLineDto;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.test.model.scm.shipments.DeliveryNote;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoListFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.ListOperation;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import java.util.Objects;

@DtoFor(DeliveryNote.class)
public class DeliveryNoteDto implements Dto<DeliveryNote> {

    public ValueWrapper<String> id = ValueWrapper.empty();
    public ValueWrapper<String> year = ValueWrapper.empty();
    public ValueWrapper<String> code = ValueWrapper.empty();
    public ValueWrapper<Set<ListOperation<DeliveryNoteLineDto>>> deliveryNoteLines = ValueWrapper.empty();

    public DeliveryNoteDto() {
    }

    public Class<DeliveryNote> getSourceClass() {
        return DeliveryNote.class;
    }

    public static class Builder {

        ValueWrapper<String> id = ValueWrapper.empty();
        ValueWrapper<String> year = ValueWrapper.empty();
        ValueWrapper<String> code = ValueWrapper.empty();
        ValueWrapper<Set<ListOperation<DeliveryNoteLineDto>>> deliveryNoteLines = ValueWrapper.empty();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder id(final ValueWrapper<String> id){
            this.id = id == null ? ValueWrapper.empty() : id;
            return this;
        }

        public Builder year(final ValueWrapper<String> year){
            this.year = year == null ? ValueWrapper.empty() : year;
            return this;
        }

        public Builder code(final ValueWrapper<String> code){
            this.code = code == null ? ValueWrapper.empty() : code;
            return this;
        }

        public Builder deliveryNoteLines(final ValueWrapper<Set<ListOperation<DeliveryNoteLineDto>>> deliveryNoteLines){
            this.deliveryNoteLines = deliveryNoteLines == null ? ValueWrapper.empty() : deliveryNoteLines;
            return this;
        }


        public DeliveryNoteDto build() {

            DeliveryNoteDto instance = new DeliveryNoteDto();
            instance.id = id;
            instance.year = year;
            instance.code = code;
            instance.deliveryNoteLines = deliveryNoteLines;

            return instance;

        }

    }

    @ProjectionFor(DeliveryNoteDto.class)
    public static class Projection extends DtoProjection<DeliveryNoteDto> {

        public FieldConf id;
        public FieldConf year;
        public FieldConf code;
        public DtoListFieldConf<DeliveryNoteLineDto.Projection> deliveryNoteLines;

        public Projection() {
        }

        public void applyProjectionTo(DeliveryNoteDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.year = ProjectionUtils.getProjectedValue(dto.year, this.year);
            dto.code = ProjectionUtils.getProjectedValue(dto.code, this.code);
            dto.deliveryNoteLines = ProjectionUtils.getProjectedValue(dto.deliveryNoteLines, this.deliveryNoteLines);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return  Objects.equals(id, that.id)
                       && Objects.equals(year, that.year)
                       && Objects.equals(code, that.code)
                       && Objects.equals(deliveryNoteLines, that.deliveryNoteLines);

        }

        @Override
        public int hashCode() {
            return Objects.hash(id,
                    year,
                    code,
                    deliveryNoteLines);
        }

        public static class Builder {

            FieldConf id;
            FieldConf year;
            FieldConf code;
            DtoListFieldConf<DeliveryNoteLineDto.Projection> deliveryNoteLines;

            public static Builder newInstance() {
                return new Builder();
            }

            public Builder id(final FieldConf id){
                this.id = id;
                return this;
            }

            public Builder id(final Presence presence){
                id = FieldConf.of(presence);
                return this;
            }

            public Builder year(final FieldConf year){
                this.year = year;
                return this;
            }

            public Builder year(final Presence presence){
                year = FieldConf.of(presence);
                return this;
            }

            public Builder code(final FieldConf code){
                this.code = code;
                return this;
            }

            public Builder code(final Presence presence){
                code = FieldConf.of(presence);
                return this;
            }

            public Builder deliveryNoteLines(final DtoListFieldConf<DeliveryNoteLineDto.Projection> deliveryNoteLines){
                this.deliveryNoteLines = deliveryNoteLines;
                return this;
            }

            public Builder deliveryNoteLines(final Presence presence, final DeliveryNoteLineDto.Projection projection){
                deliveryNoteLines = DtoListFieldConf.of(presence, projection);
                return this;
            }

            public DeliveryNoteDto.Projection build() {

                DeliveryNoteDto.Projection instance = new DeliveryNoteDto.Projection();
                instance.id = id;
                instance.year = year;
                instance.code = code;
                instance.deliveryNoteLines = deliveryNoteLines;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _id = "id";
        public static final String _year = "year";
        public static final String _code = "code";
        public static final String _deliveryNoteLines = "deliveryNoteLines";

    }

}