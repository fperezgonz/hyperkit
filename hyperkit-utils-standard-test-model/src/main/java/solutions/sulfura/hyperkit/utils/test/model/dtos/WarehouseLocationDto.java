package solutions.sulfura.hyperkit.utils.test.model.dtos;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.utils.test.model.dtos.WarehouseDto;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.utils.test.model.scm.inventory.WarehouseLocation;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import java.util.Objects;

@DtoFor(WarehouseLocation.class)
public class WarehouseLocationDto implements Dto<WarehouseLocation> {

    public ValueWrapper<String> id = ValueWrapper.empty();
    public ValueWrapper<String> name = ValueWrapper.empty();
    public ValueWrapper<WarehouseDto> warehouse = ValueWrapper.empty();

    public WarehouseLocationDto() {
    }

    public Class<WarehouseLocation> getSourceClass() {
        return WarehouseLocation.class;
    }

    public static class Builder {

        ValueWrapper<String> id = ValueWrapper.empty();
        ValueWrapper<String> name = ValueWrapper.empty();
        ValueWrapper<WarehouseDto> warehouse = ValueWrapper.empty();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder id(final ValueWrapper<String> id){
            this.id = id == null ? ValueWrapper.empty() : id;
            return this;
        }

        public Builder name(final ValueWrapper<String> name){
            this.name = name == null ? ValueWrapper.empty() : name;
            return this;
        }

        public Builder warehouse(final ValueWrapper<WarehouseDto> warehouse){
            this.warehouse = warehouse == null ? ValueWrapper.empty() : warehouse;
            return this;
        }


        public WarehouseLocationDto build() {

            WarehouseLocationDto instance = new WarehouseLocationDto();
            instance.id = id;
            instance.name = name;
            instance.warehouse = warehouse;

            return instance;

        }

    }

    @ProjectionFor(WarehouseLocationDto.class)
    public static class Projection extends DtoProjection<WarehouseLocationDto> {

        public FieldConf id;
        public FieldConf name;
        public DtoFieldConf<WarehouseDto.Projection> warehouse;

        public Projection() {
        }

        public void applyProjectionTo(WarehouseLocationDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.name = ProjectionUtils.getProjectedValue(dto.name, this.name);
            dto.warehouse = ProjectionUtils.getProjectedValue(dto.warehouse, this.warehouse);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return  Objects.equals(id, that.id)
                       && Objects.equals(name, that.name)
                       && Objects.equals(warehouse, that.warehouse);

        }

        @Override
        public int hashCode() {
            return Objects.hash(id,
                    name,
                    warehouse);
        }

        public static class Builder {

            FieldConf id;
            FieldConf name;
            DtoFieldConf<WarehouseDto.Projection> warehouse;

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

            public Builder name(final FieldConf name){
                this.name = name;
                return this;
            }

            public Builder name(final Presence presence){
                name = FieldConf.of(presence);
                return this;
            }

            public Builder warehouse(final DtoFieldConf<WarehouseDto.Projection> warehouse){
                this.warehouse = warehouse;
                return this;
            }

            public Builder warehouse(final Presence presence, final WarehouseDto.Projection projection){
                warehouse = DtoFieldConf.of(presence, projection);
                return this;
            }

            public WarehouseLocationDto.Projection build() {

                WarehouseLocationDto.Projection instance = new WarehouseLocationDto.Projection();
                instance.id = id;
                instance.name = name;
                instance.warehouse = warehouse;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _id = "id";
        public static final String _name = "name";
        public static final String _warehouse = "warehouse";

    }

}