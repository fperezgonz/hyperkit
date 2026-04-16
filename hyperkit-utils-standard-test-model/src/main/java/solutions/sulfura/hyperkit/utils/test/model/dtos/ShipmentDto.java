package solutions.sulfura.hyperkit.utils.test.model.dtos;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.test.model.scm.shipments.Shipment;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.utils.test.model.dtos.SupplierRefDto;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import java.util.Objects;

@DtoFor(Shipment.class)
public class ShipmentDto implements Dto<Shipment> {

    public ValueWrapper<String> id = ValueWrapper.empty();
    public ValueWrapper<String> trackingNumber = ValueWrapper.empty();
    public ValueWrapper<String> status = ValueWrapper.empty();
    public ValueWrapper<SupplierRefDto> supplierRef = ValueWrapper.empty();

    public ShipmentDto() {
    }

    public Class<Shipment> getSourceClass() {
        return Shipment.class;
    }

    public static class Builder {

        ValueWrapper<String> id = ValueWrapper.empty();
        ValueWrapper<String> trackingNumber = ValueWrapper.empty();
        ValueWrapper<String> status = ValueWrapper.empty();
        ValueWrapper<SupplierRefDto> supplierRef = ValueWrapper.empty();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder id(final ValueWrapper<String> id){
            this.id = id == null ? ValueWrapper.empty() : id;
            return this;
        }

        public Builder trackingNumber(final ValueWrapper<String> trackingNumber){
            this.trackingNumber = trackingNumber == null ? ValueWrapper.empty() : trackingNumber;
            return this;
        }

        public Builder status(final ValueWrapper<String> status){
            this.status = status == null ? ValueWrapper.empty() : status;
            return this;
        }

        public Builder supplierRef(final ValueWrapper<SupplierRefDto> supplierRef){
            this.supplierRef = supplierRef == null ? ValueWrapper.empty() : supplierRef;
            return this;
        }


        public ShipmentDto build() {

            ShipmentDto instance = new ShipmentDto();
            instance.id = id;
            instance.trackingNumber = trackingNumber;
            instance.status = status;
            instance.supplierRef = supplierRef;

            return instance;

        }

    }

    @ProjectionFor(ShipmentDto.class)
    public static class Projection extends DtoProjection<ShipmentDto> {

        public FieldConf id;
        public FieldConf trackingNumber;
        public FieldConf status;
        public DtoFieldConf<SupplierRefDto.Projection> supplierRef;

        public Projection() {
        }

        public void applyProjectionTo(ShipmentDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.trackingNumber = ProjectionUtils.getProjectedValue(dto.trackingNumber, this.trackingNumber);
            dto.status = ProjectionUtils.getProjectedValue(dto.status, this.status);
            dto.supplierRef = ProjectionUtils.getProjectedValue(dto.supplierRef, this.supplierRef);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return  Objects.equals(id, that.id)
                       && Objects.equals(trackingNumber, that.trackingNumber)
                       && Objects.equals(status, that.status)
                       && Objects.equals(supplierRef, that.supplierRef);

        }

        @Override
        public int hashCode() {
            return Objects.hash(id,
                    trackingNumber,
                    status,
                    supplierRef);
        }

        public static class Builder {

            FieldConf id;
            FieldConf trackingNumber;
            FieldConf status;
            DtoFieldConf<SupplierRefDto.Projection> supplierRef;

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

            public Builder trackingNumber(final FieldConf trackingNumber){
                this.trackingNumber = trackingNumber;
                return this;
            }

            public Builder trackingNumber(final Presence presence){
                trackingNumber = FieldConf.of(presence);
                return this;
            }

            public Builder status(final FieldConf status){
                this.status = status;
                return this;
            }

            public Builder status(final Presence presence){
                status = FieldConf.of(presence);
                return this;
            }

            public Builder supplierRef(final DtoFieldConf<SupplierRefDto.Projection> supplierRef){
                this.supplierRef = supplierRef;
                return this;
            }

            public Builder supplierRef(final Presence presence, final SupplierRefDto.Projection projection){
                supplierRef = DtoFieldConf.of(presence, projection);
                return this;
            }

            public ShipmentDto.Projection build() {

                ShipmentDto.Projection instance = new ShipmentDto.Projection();
                instance.id = id;
                instance.trackingNumber = trackingNumber;
                instance.status = status;
                instance.supplierRef = supplierRef;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _id = "id";
        public static final String _trackingNumber = "trackingNumber";
        public static final String _status = "status";
        public static final String _supplierRef = "supplierRef";

    }

}