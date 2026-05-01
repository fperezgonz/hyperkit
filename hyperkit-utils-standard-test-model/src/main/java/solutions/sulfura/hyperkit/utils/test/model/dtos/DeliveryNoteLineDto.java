package solutions.sulfura.hyperkit.utils.test.model.dtos;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.test.model.dtos.DeliveryNoteDto;
import solutions.sulfura.hyperkit.utils.test.model.scm.shipments.DeliveryNoteLine;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import java.util.Objects;

@DtoFor(DeliveryNoteLine.class)
public class DeliveryNoteLineDto implements Dto<DeliveryNoteLine> {

    public ValueWrapper<String> id = ValueWrapper.empty();
    public ValueWrapper<String> code = ValueWrapper.empty();
    public ValueWrapper<String> concept = ValueWrapper.empty();
    public ValueWrapper<DeliveryNoteDto> deliveryNote = ValueWrapper.empty();

    public DeliveryNoteLineDto() {
    }

    public Class<DeliveryNoteLine> getSourceClass() {
        return DeliveryNoteLine.class;
    }

    public static class Builder {

        ValueWrapper<String> id = ValueWrapper.empty();
        ValueWrapper<String> code = ValueWrapper.empty();
        ValueWrapper<String> concept = ValueWrapper.empty();
        ValueWrapper<DeliveryNoteDto> deliveryNote = ValueWrapper.empty();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder id(final ValueWrapper<String> id){
            this.id = id == null ? ValueWrapper.empty() : id;
            return this;
        }

        public Builder code(final ValueWrapper<String> code){
            this.code = code == null ? ValueWrapper.empty() : code;
            return this;
        }

        public Builder concept(final ValueWrapper<String> concept){
            this.concept = concept == null ? ValueWrapper.empty() : concept;
            return this;
        }

        public Builder deliveryNote(final ValueWrapper<DeliveryNoteDto> deliveryNote){
            this.deliveryNote = deliveryNote == null ? ValueWrapper.empty() : deliveryNote;
            return this;
        }


        public DeliveryNoteLineDto build() {

            DeliveryNoteLineDto instance = new DeliveryNoteLineDto();
            instance.id = id;
            instance.code = code;
            instance.concept = concept;
            instance.deliveryNote = deliveryNote;

            return instance;

        }

    }

    @ProjectionFor(DeliveryNoteLineDto.class)
    public static class Projection extends DtoProjection<DeliveryNoteLineDto> {

        public FieldConf id;
        public FieldConf code;
        public FieldConf concept;
        public DtoFieldConf<DeliveryNoteDto.Projection> deliveryNote;

        public Projection() {
        }

        public void applyProjectionTo(DeliveryNoteLineDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.code = ProjectionUtils.getProjectedValue(dto.code, this.code);
            dto.concept = ProjectionUtils.getProjectedValue(dto.concept, this.concept);
            dto.deliveryNote = ProjectionUtils.getProjectedValue(dto.deliveryNote, this.deliveryNote);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return  Objects.equals(id, that.id)
                       && Objects.equals(code, that.code)
                       && Objects.equals(concept, that.concept)
                       && Objects.equals(deliveryNote, that.deliveryNote);

        }

        @Override
        public int hashCode() {
            return Objects.hash(id,
                    code,
                    concept,
                    deliveryNote);
        }

        public static class Builder {

            FieldConf id;
            FieldConf code;
            FieldConf concept;
            DtoFieldConf<DeliveryNoteDto.Projection> deliveryNote;

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

            public Builder code(final FieldConf code){
                this.code = code;
                return this;
            }

            public Builder code(final Presence presence){
                code = FieldConf.of(presence);
                return this;
            }

            public Builder concept(final FieldConf concept){
                this.concept = concept;
                return this;
            }

            public Builder concept(final Presence presence){
                concept = FieldConf.of(presence);
                return this;
            }

            public Builder deliveryNote(final DtoFieldConf<DeliveryNoteDto.Projection> deliveryNote){
                this.deliveryNote = deliveryNote;
                return this;
            }

            public Builder deliveryNote(final Presence presence, final DeliveryNoteDto.Projection projection){
                deliveryNote = DtoFieldConf.of(presence, projection);
                return this;
            }

            public DeliveryNoteLineDto.Projection build() {

                DeliveryNoteLineDto.Projection instance = new DeliveryNoteLineDto.Projection();
                instance.id = id;
                instance.code = code;
                instance.concept = concept;
                instance.deliveryNote = deliveryNote;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _id = "id";
        public static final String _code = "code";
        public static final String _concept = "concept";
        public static final String _deliveryNote = "deliveryNote";

    }

}