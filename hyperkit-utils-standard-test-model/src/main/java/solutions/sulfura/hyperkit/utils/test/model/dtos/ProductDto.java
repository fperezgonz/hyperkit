package solutions.sulfura.hyperkit.utils.test.model.dtos;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.utils.test.model.scm.inventory.Product;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import java.util.Objects;

@DtoFor(Product.class)
public class ProductDto implements Dto<Product> {

    public ValueWrapper<String> id = ValueWrapper.empty();
    public ValueWrapper<String> sku = ValueWrapper.empty();
    public ValueWrapper<String> name = ValueWrapper.empty();

    public ProductDto() {
    }

    public Class<Product> getSourceClass() {
        return Product.class;
    }

    public static class Builder {

        ValueWrapper<String> id = ValueWrapper.empty();
        ValueWrapper<String> sku = ValueWrapper.empty();
        ValueWrapper<String> name = ValueWrapper.empty();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder id(final ValueWrapper<String> id){
            this.id = id == null ? ValueWrapper.empty() : id;
            return this;
        }

        public Builder sku(final ValueWrapper<String> sku){
            this.sku = sku == null ? ValueWrapper.empty() : sku;
            return this;
        }

        public Builder name(final ValueWrapper<String> name){
            this.name = name == null ? ValueWrapper.empty() : name;
            return this;
        }


        public ProductDto build() {

            ProductDto instance = new ProductDto();
            instance.id = id;
            instance.sku = sku;
            instance.name = name;

            return instance;

        }

    }

    @ProjectionFor(ProductDto.class)
    public static class Projection extends DtoProjection<ProductDto> {

        public FieldConf id;
        public FieldConf sku;
        public FieldConf name;

        public Projection() {
        }

        public void applyProjectionTo(ProductDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.sku = ProjectionUtils.getProjectedValue(dto.sku, this.sku);
            dto.name = ProjectionUtils.getProjectedValue(dto.name, this.name);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return  Objects.equals(id, that.id)
                       && Objects.equals(sku, that.sku)
                       && Objects.equals(name, that.name);

        }

        @Override
        public int hashCode() {
            return Objects.hash(id,
                    sku,
                    name);
        }

        public static class Builder {

            FieldConf id;
            FieldConf sku;
            FieldConf name;

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

            public Builder sku(final FieldConf sku){
                this.sku = sku;
                return this;
            }

            public Builder sku(final Presence presence){
                sku = FieldConf.of(presence);
                return this;
            }

            public Builder name(final FieldConf name){
                this.name = name;
                return this;
            }

            public Builder name(final Presence presence){
                name = FieldConf.of(presence);
                return this;
            }

            public ProductDto.Projection build() {

                ProductDto.Projection instance = new ProductDto.Projection();
                instance.id = id;
                instance.sku = sku;
                instance.name = name;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _id = "id";
        public static final String _sku = "sku";
        public static final String _name = "name";

    }

}