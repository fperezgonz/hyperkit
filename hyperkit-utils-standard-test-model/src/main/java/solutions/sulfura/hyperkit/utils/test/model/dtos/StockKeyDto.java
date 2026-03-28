package solutions.sulfura.hyperkit.utils.test.model.dtos;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.test.model.scm.inventory.Stock.StockKey;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.utils.test.model.dtos.WarehouseLocationDto;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.utils.test.model.scm.inventory.Stock;
import solutions.sulfura.hyperkit.utils.test.model.dtos.ProductDto;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import java.util.Objects;

@DtoFor(Stock.StockKey.class)
public class StockKeyDto implements Dto<Stock.StockKey> {

    public ValueWrapper<WarehouseLocationDto> warehouseLocation = ValueWrapper.empty();
    public ValueWrapper<ProductDto> product = ValueWrapper.empty();

    public StockKeyDto() {
    }

    public Class<Stock.StockKey> getSourceClass() {
        return Stock.StockKey.class;
    }

    public static class Builder {

        ValueWrapper<WarehouseLocationDto> warehouseLocation = ValueWrapper.empty();
        ValueWrapper<ProductDto> product = ValueWrapper.empty();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder warehouseLocation(final ValueWrapper<WarehouseLocationDto> warehouseLocation){
            this.warehouseLocation = warehouseLocation == null ? ValueWrapper.empty() : warehouseLocation;
            return this;
        }

        public Builder product(final ValueWrapper<ProductDto> product){
            this.product = product == null ? ValueWrapper.empty() : product;
            return this;
        }


        public StockKeyDto build() {

            StockKeyDto instance = new StockKeyDto();
            instance.warehouseLocation = warehouseLocation;
            instance.product = product;

            return instance;

        }

    }

    @ProjectionFor(StockKeyDto.class)
    public static class Projection extends DtoProjection<StockKeyDto> {

        public DtoFieldConf<ProductDto.Projection> warehouseLocation;
        public DtoFieldConf<ProductDto.Projection> product;

        public Projection() {
        }

        public void applyProjectionTo(StockKeyDto dto) throws DtoProjectionException {
            dto.warehouseLocation = ProjectionUtils.getProjectedValue(dto.warehouseLocation, this.warehouseLocation);
            dto.product = ProjectionUtils.getProjectedValue(dto.product, this.product);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return  Objects.equals(warehouseLocation, that.warehouseLocation)
                       && Objects.equals(product, that.product);

        }

        @Override
        public int hashCode() {
            return Objects.hash(warehouseLocation,
                    product);
        }

        public static class Builder {

            DtoFieldConf<ProductDto.Projection> warehouseLocation;
            DtoFieldConf<ProductDto.Projection> product;

            public static Builder newInstance() {
                return new Builder();
            }

            public Builder warehouseLocation(final DtoFieldConf<ProductDto.Projection> warehouseLocation){
                this.warehouseLocation = warehouseLocation;
                return this;
            }

            public Builder warehouseLocation(final Presence presence, final ProductDto.Projection projection){
                warehouseLocation = DtoFieldConf.of(presence, projection);
                return this;
            }

            public Builder product(final DtoFieldConf<ProductDto.Projection> product){
                this.product = product;
                return this;
            }

            public Builder product(final Presence presence, final ProductDto.Projection projection){
                product = DtoFieldConf.of(presence, projection);
                return this;
            }

            public StockKeyDto.Projection build() {

                StockKeyDto.Projection instance = new StockKeyDto.Projection();
                instance.warehouseLocation = warehouseLocation;
                instance.product = product;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _warehouseLocation = "warehouseLocation";
        public static final String _product = "product";

    }

}