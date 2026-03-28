package solutions.sulfura.hyperkit.utils.test.model.dtos;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import java.math.BigDecimal;
import solutions.sulfura.hyperkit.utils.test.model.scm.inventory.Stock.CompositeKey;
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

@DtoFor(Stock.class)
public class StockDto implements Dto<Stock> {

    public ValueWrapper<CompositeKey> id = ValueWrapper.empty();
    public ValueWrapper<WarehouseLocationDto> location = ValueWrapper.empty();
    public ValueWrapper<ProductDto> product = ValueWrapper.empty();
    public ValueWrapper<BigDecimal> quantity = ValueWrapper.empty();

    public StockDto() {
    }

    public Class<Stock> getSourceClass() {
        return Stock.class;
    }

    public static class Builder {

        ValueWrapper<CompositeKey> id = ValueWrapper.empty();
        ValueWrapper<WarehouseLocationDto> location = ValueWrapper.empty();
        ValueWrapper<ProductDto> product = ValueWrapper.empty();
        ValueWrapper<BigDecimal> quantity = ValueWrapper.empty();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder id(final ValueWrapper<CompositeKey> id){
            this.id = id == null ? ValueWrapper.empty() : id;
            return this;
        }

        public Builder location(final ValueWrapper<WarehouseLocationDto> location){
            this.location = location == null ? ValueWrapper.empty() : location;
            return this;
        }

        public Builder product(final ValueWrapper<ProductDto> product){
            this.product = product == null ? ValueWrapper.empty() : product;
            return this;
        }

        public Builder quantity(final ValueWrapper<BigDecimal> quantity){
            this.quantity = quantity == null ? ValueWrapper.empty() : quantity;
            return this;
        }


        public StockDto build() {

            StockDto instance = new StockDto();
            instance.id = id;
            instance.location = location;
            instance.product = product;
            instance.quantity = quantity;

            return instance;

        }

    }

    @ProjectionFor(StockDto.class)
    public static class Projection extends DtoProjection<StockDto> {

        public FieldConf id;
        public DtoFieldConf<ProductDto.Projection> location;
        public DtoFieldConf<ProductDto.Projection> product;
        public FieldConf quantity;

        public Projection() {
        }

        public void applyProjectionTo(StockDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.location = ProjectionUtils.getProjectedValue(dto.location, this.location);
            dto.product = ProjectionUtils.getProjectedValue(dto.product, this.product);
            dto.quantity = ProjectionUtils.getProjectedValue(dto.quantity, this.quantity);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return  Objects.equals(id, that.id)
                       && Objects.equals(location, that.location)
                       && Objects.equals(product, that.product)
                       && Objects.equals(quantity, that.quantity);

        }

        @Override
        public int hashCode() {
            return Objects.hash(id,
                    location,
                    product,
                    quantity);
        }

        public static class Builder {

            FieldConf id;
            DtoFieldConf<ProductDto.Projection> location;
            DtoFieldConf<ProductDto.Projection> product;
            FieldConf quantity;

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

            public Builder location(final DtoFieldConf<ProductDto.Projection> location){
                this.location = location;
                return this;
            }

            public Builder location(final Presence presence, final ProductDto.Projection projection){
                location = DtoFieldConf.of(presence, projection);
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

            public Builder quantity(final FieldConf quantity){
                this.quantity = quantity;
                return this;
            }

            public Builder quantity(final Presence presence){
                quantity = FieldConf.of(presence);
                return this;
            }

            public StockDto.Projection build() {

                StockDto.Projection instance = new StockDto.Projection();
                instance.id = id;
                instance.location = location;
                instance.product = product;
                instance.quantity = quantity;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _id = "id";
        public static final String _location = "location";
        public static final String _product = "product";
        public static final String _quantity = "quantity";

    }

}