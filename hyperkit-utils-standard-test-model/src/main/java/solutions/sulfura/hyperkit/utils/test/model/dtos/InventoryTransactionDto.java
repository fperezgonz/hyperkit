package solutions.sulfura.hyperkit.utils.test.model.dtos;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.utils.test.model.dtos.WarehouseLocationDto;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.utils.test.model.scm.inventory.InventoryTransaction;
import solutions.sulfura.hyperkit.utils.test.model.dtos.ProductDto;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import java.util.Objects;

@DtoFor(InventoryTransaction.class)
public class InventoryTransactionDto implements Dto<InventoryTransaction> {

    public ValueWrapper<String> id = ValueWrapper.empty();
    public ValueWrapper<ProductDto> product = ValueWrapper.empty();
    public ValueWrapper<String> unitOfMeasure = ValueWrapper.empty();
    public ValueWrapper<Integer> quantity = ValueWrapper.empty();
    public ValueWrapper<WarehouseLocationDto> fromLocation = ValueWrapper.empty();
    public ValueWrapper<WarehouseLocationDto> toLocation = ValueWrapper.empty();

    public InventoryTransactionDto() {
    }

    public Class<InventoryTransaction> getSourceClass() {
        return InventoryTransaction.class;
    }

    public static class Builder {

        ValueWrapper<String> id = ValueWrapper.empty();
        ValueWrapper<ProductDto> product = ValueWrapper.empty();
        ValueWrapper<String> unitOfMeasure = ValueWrapper.empty();
        ValueWrapper<Integer> quantity = ValueWrapper.empty();
        ValueWrapper<WarehouseLocationDto> fromLocation = ValueWrapper.empty();
        ValueWrapper<WarehouseLocationDto> toLocation = ValueWrapper.empty();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder id(final ValueWrapper<String> id){
            this.id = id == null ? ValueWrapper.empty() : id;
            return this;
        }

        public Builder product(final ValueWrapper<ProductDto> product){
            this.product = product == null ? ValueWrapper.empty() : product;
            return this;
        }

        public Builder unitOfMeasure(final ValueWrapper<String> unitOfMeasure){
            this.unitOfMeasure = unitOfMeasure == null ? ValueWrapper.empty() : unitOfMeasure;
            return this;
        }

        public Builder quantity(final ValueWrapper<Integer> quantity){
            this.quantity = quantity == null ? ValueWrapper.empty() : quantity;
            return this;
        }

        public Builder fromLocation(final ValueWrapper<WarehouseLocationDto> fromLocation){
            this.fromLocation = fromLocation == null ? ValueWrapper.empty() : fromLocation;
            return this;
        }

        public Builder toLocation(final ValueWrapper<WarehouseLocationDto> toLocation){
            this.toLocation = toLocation == null ? ValueWrapper.empty() : toLocation;
            return this;
        }


        public InventoryTransactionDto build() {

            InventoryTransactionDto instance = new InventoryTransactionDto();
            instance.id = id;
            instance.product = product;
            instance.unitOfMeasure = unitOfMeasure;
            instance.quantity = quantity;
            instance.fromLocation = fromLocation;
            instance.toLocation = toLocation;

            return instance;

        }

    }

    @ProjectionFor(InventoryTransactionDto.class)
    public static class Projection extends DtoProjection<InventoryTransactionDto> {

        public FieldConf id;
        public DtoFieldConf<WarehouseLocationDto.Projection> product;
        public FieldConf unitOfMeasure;
        public FieldConf quantity;
        public DtoFieldConf<WarehouseLocationDto.Projection> fromLocation;
        public DtoFieldConf<WarehouseLocationDto.Projection> toLocation;

        public Projection() {
        }

        public void applyProjectionTo(InventoryTransactionDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.product = ProjectionUtils.getProjectedValue(dto.product, this.product);
            dto.unitOfMeasure = ProjectionUtils.getProjectedValue(dto.unitOfMeasure, this.unitOfMeasure);
            dto.quantity = ProjectionUtils.getProjectedValue(dto.quantity, this.quantity);
            dto.fromLocation = ProjectionUtils.getProjectedValue(dto.fromLocation, this.fromLocation);
            dto.toLocation = ProjectionUtils.getProjectedValue(dto.toLocation, this.toLocation);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return  Objects.equals(id, that.id)
                       && Objects.equals(product, that.product)
                       && Objects.equals(unitOfMeasure, that.unitOfMeasure)
                       && Objects.equals(quantity, that.quantity)
                       && Objects.equals(fromLocation, that.fromLocation)
                       && Objects.equals(toLocation, that.toLocation);

        }

        @Override
        public int hashCode() {
            return Objects.hash(id,
                    product,
                    unitOfMeasure,
                    quantity,
                    fromLocation,
                    toLocation);
        }

        public static class Builder {

            FieldConf id;
            DtoFieldConf<WarehouseLocationDto.Projection> product;
            FieldConf unitOfMeasure;
            FieldConf quantity;
            DtoFieldConf<WarehouseLocationDto.Projection> fromLocation;
            DtoFieldConf<WarehouseLocationDto.Projection> toLocation;

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

            public Builder product(final DtoFieldConf<WarehouseLocationDto.Projection> product){
                this.product = product;
                return this;
            }

            public Builder product(final Presence presence, final WarehouseLocationDto.Projection projection){
                product = DtoFieldConf.of(presence, projection);
                return this;
            }

            public Builder unitOfMeasure(final FieldConf unitOfMeasure){
                this.unitOfMeasure = unitOfMeasure;
                return this;
            }

            public Builder unitOfMeasure(final Presence presence){
                unitOfMeasure = FieldConf.of(presence);
                return this;
            }

            public Builder quantity(final FieldConf quantity){
                this.quantity = quantity;
                return this;
            }

            public Builder quantity(final Presence presence){
                quantity = FieldConf.of(presence);
                return this;
            }

            public Builder fromLocation(final DtoFieldConf<WarehouseLocationDto.Projection> fromLocation){
                this.fromLocation = fromLocation;
                return this;
            }

            public Builder fromLocation(final Presence presence, final WarehouseLocationDto.Projection projection){
                fromLocation = DtoFieldConf.of(presence, projection);
                return this;
            }

            public Builder toLocation(final DtoFieldConf<WarehouseLocationDto.Projection> toLocation){
                this.toLocation = toLocation;
                return this;
            }

            public Builder toLocation(final Presence presence, final WarehouseLocationDto.Projection projection){
                toLocation = DtoFieldConf.of(presence, projection);
                return this;
            }

            public InventoryTransactionDto.Projection build() {

                InventoryTransactionDto.Projection instance = new InventoryTransactionDto.Projection();
                instance.id = id;
                instance.product = product;
                instance.unitOfMeasure = unitOfMeasure;
                instance.quantity = quantity;
                instance.fromLocation = fromLocation;
                instance.toLocation = toLocation;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _id = "id";
        public static final String _product = "product";
        public static final String _unitOfMeasure = "unitOfMeasure";
        public static final String _quantity = "quantity";
        public static final String _fromLocation = "fromLocation";
        public static final String _toLocation = "toLocation";

    }

}