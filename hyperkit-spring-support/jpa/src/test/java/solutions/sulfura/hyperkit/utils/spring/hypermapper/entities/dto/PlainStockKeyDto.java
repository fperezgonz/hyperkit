package solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.dto;

import solutions.sulfura.hyperkit.utils.spring.hypermapper.CompositeIdMappingTests.StockWithPlainCompositeKey.PlainStockKey;
import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.CompositeIdMappingTests;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.CompositeIdMappingTests.StockWithPlainCompositeKey;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import java.util.Objects;

@DtoFor(CompositeIdMappingTests.StockWithPlainCompositeKey.PlainStockKey.class)
public class PlainStockKeyDto implements Dto<CompositeIdMappingTests.StockWithPlainCompositeKey.PlainStockKey> {

    public ValueWrapper<String> warehouseLocationId = ValueWrapper.empty();
    public ValueWrapper<String> productId = ValueWrapper.empty();

    public PlainStockKeyDto() {
    }

    public Class<CompositeIdMappingTests.StockWithPlainCompositeKey.PlainStockKey> getSourceClass() {
        return CompositeIdMappingTests.StockWithPlainCompositeKey.PlainStockKey.class;
    }

    public static class Builder {

        ValueWrapper<String> warehouseLocationId = ValueWrapper.empty();
        ValueWrapper<String> productId = ValueWrapper.empty();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder warehouseLocationId(final ValueWrapper<String> warehouseLocationId){
            this.warehouseLocationId = warehouseLocationId == null ? ValueWrapper.empty() : warehouseLocationId;
            return this;
        }

        public Builder productId(final ValueWrapper<String> productId){
            this.productId = productId == null ? ValueWrapper.empty() : productId;
            return this;
        }


        public PlainStockKeyDto build() {

            PlainStockKeyDto instance = new PlainStockKeyDto();
            instance.warehouseLocationId = warehouseLocationId;
            instance.productId = productId;

            return instance;

        }

    }

    @ProjectionFor(PlainStockKeyDto.class)
    public static class Projection extends DtoProjection<PlainStockKeyDto> {

        public FieldConf warehouseLocationId;
        public FieldConf productId;

        public Projection() {
        }

        public void applyProjectionTo(PlainStockKeyDto dto) throws DtoProjectionException {
            dto.warehouseLocationId = ProjectionUtils.getProjectedValue(dto.warehouseLocationId, this.warehouseLocationId);
            dto.productId = ProjectionUtils.getProjectedValue(dto.productId, this.productId);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return  Objects.equals(warehouseLocationId, that.warehouseLocationId)
                       && Objects.equals(productId, that.productId);

        }

        @Override
        public int hashCode() {
            return Objects.hash(warehouseLocationId,
                    productId);
        }

        public static class Builder {

            FieldConf warehouseLocationId;
            FieldConf productId;

            public static Builder newInstance() {
                return new Builder();
            }

            public Builder warehouseLocationId(final FieldConf warehouseLocationId){
                this.warehouseLocationId = warehouseLocationId;
                return this;
            }

            public Builder warehouseLocationId(final Presence presence){
                warehouseLocationId = FieldConf.of(presence);
                return this;
            }

            public Builder productId(final FieldConf productId){
                this.productId = productId;
                return this;
            }

            public Builder productId(final Presence presence){
                productId = FieldConf.of(presence);
                return this;
            }

            public PlainStockKeyDto.Projection build() {

                PlainStockKeyDto.Projection instance = new PlainStockKeyDto.Projection();
                instance.warehouseLocationId = warehouseLocationId;
                instance.productId = productId;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _warehouseLocationId = "warehouseLocationId";
        public static final String _productId = "productId";

    }

}