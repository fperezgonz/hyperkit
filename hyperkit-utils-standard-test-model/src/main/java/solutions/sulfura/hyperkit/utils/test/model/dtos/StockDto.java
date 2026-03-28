package solutions.sulfura.hyperkit.utils.test.model.dtos;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import java.math.BigDecimal;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.utils.test.model.dtos.StockKeyDto;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.utils.test.model.scm.inventory.Stock;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import java.util.Objects;

@DtoFor(Stock.class)
public class StockDto implements Dto<Stock> {

    public ValueWrapper<StockKeyDto> id = ValueWrapper.empty();
    public ValueWrapper<BigDecimal> quantity = ValueWrapper.empty();

    public StockDto() {
    }

    public Class<Stock> getSourceClass() {
        return Stock.class;
    }

    public static class Builder {

        ValueWrapper<StockKeyDto> id = ValueWrapper.empty();
        ValueWrapper<BigDecimal> quantity = ValueWrapper.empty();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder id(final ValueWrapper<StockKeyDto> id){
            this.id = id == null ? ValueWrapper.empty() : id;
            return this;
        }

        public Builder quantity(final ValueWrapper<BigDecimal> quantity){
            this.quantity = quantity == null ? ValueWrapper.empty() : quantity;
            return this;
        }


        public StockDto build() {

            StockDto instance = new StockDto();
            instance.id = id;
            instance.quantity = quantity;

            return instance;

        }

    }

    @ProjectionFor(StockDto.class)
    public static class Projection extends DtoProjection<StockDto> {

        public DtoFieldConf<StockKeyDto.Projection> id;
        public FieldConf quantity;

        public Projection() {
        }

        public void applyProjectionTo(StockDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.quantity = ProjectionUtils.getProjectedValue(dto.quantity, this.quantity);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return  Objects.equals(id, that.id)
                       && Objects.equals(quantity, that.quantity);

        }

        @Override
        public int hashCode() {
            return Objects.hash(id,
                    quantity);
        }

        public static class Builder {

            DtoFieldConf<StockKeyDto.Projection> id;
            FieldConf quantity;

            public static Builder newInstance() {
                return new Builder();
            }

            public Builder id(final DtoFieldConf<StockKeyDto.Projection> id){
                this.id = id;
                return this;
            }

            public Builder id(final Presence presence, final StockKeyDto.Projection projection){
                id = DtoFieldConf.of(presence, projection);
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
                instance.quantity = quantity;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _id = "id";
        public static final String _quantity = "quantity";

    }

}