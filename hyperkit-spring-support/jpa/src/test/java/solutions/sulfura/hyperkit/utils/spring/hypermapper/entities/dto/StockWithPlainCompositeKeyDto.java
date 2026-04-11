package solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.dto;

import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.dto.PlainStockKeyDto;
import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.CompositeIdMappingTests;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import java.math.BigDecimal;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.CompositeIdMappingTests.StockWithPlainCompositeKey;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import java.util.Objects;

@DtoFor(CompositeIdMappingTests.StockWithPlainCompositeKey.class)
public class StockWithPlainCompositeKeyDto implements Dto<CompositeIdMappingTests.StockWithPlainCompositeKey> {

    public ValueWrapper<PlainStockKeyDto> id = ValueWrapper.empty();
    public ValueWrapper<BigDecimal> quantity = ValueWrapper.empty();

    public StockWithPlainCompositeKeyDto() {
    }

    public Class<CompositeIdMappingTests.StockWithPlainCompositeKey> getSourceClass() {
        return CompositeIdMappingTests.StockWithPlainCompositeKey.class;
    }

    public static class Builder {

        ValueWrapper<PlainStockKeyDto> id = ValueWrapper.empty();
        ValueWrapper<BigDecimal> quantity = ValueWrapper.empty();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder id(final ValueWrapper<PlainStockKeyDto> id){
            this.id = id == null ? ValueWrapper.empty() : id;
            return this;
        }

        public Builder quantity(final ValueWrapper<BigDecimal> quantity){
            this.quantity = quantity == null ? ValueWrapper.empty() : quantity;
            return this;
        }


        public StockWithPlainCompositeKeyDto build() {

            StockWithPlainCompositeKeyDto instance = new StockWithPlainCompositeKeyDto();
            instance.id = id;
            instance.quantity = quantity;

            return instance;

        }

    }

    @ProjectionFor(StockWithPlainCompositeKeyDto.class)
    public static class Projection extends DtoProjection<StockWithPlainCompositeKeyDto> {

        public DtoFieldConf<PlainStockKeyDto.Projection> id;
        public FieldConf quantity;

        public Projection() {
        }

        public void applyProjectionTo(StockWithPlainCompositeKeyDto dto) throws DtoProjectionException {
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

            DtoFieldConf<PlainStockKeyDto.Projection> id;
            FieldConf quantity;

            public static Builder newInstance() {
                return new Builder();
            }

            public Builder id(final DtoFieldConf<PlainStockKeyDto.Projection> id){
                this.id = id;
                return this;
            }

            public Builder id(final Presence presence, final PlainStockKeyDto.Projection projection){
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

            public StockWithPlainCompositeKeyDto.Projection build() {

                StockWithPlainCompositeKeyDto.Projection instance = new StockWithPlainCompositeKeyDto.Projection();
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