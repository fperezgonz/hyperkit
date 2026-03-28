package solutions.sulfura.hyperkit.utils.test.model.dtos;

import solutions.sulfura.hyperkit.dtos.Dto;
import java.util.Set;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.test.model.scm.inventory.Warehouse;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoListFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.ListOperation;
import solutions.sulfura.hyperkit.utils.test.model.dtos.WarehouseLocationDto;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import java.util.Objects;

@DtoFor(Warehouse.class)
public class WarehouseDto implements Dto<Warehouse> {

    public ValueWrapper<String> id = ValueWrapper.empty();
    public ValueWrapper<String> name = ValueWrapper.empty();
    public ValueWrapper<Set<ListOperation<WarehouseLocationDto>>> locations = ValueWrapper.empty();

    public WarehouseDto() {
    }

    public Class<Warehouse> getSourceClass() {
        return Warehouse.class;
    }

    public static class Builder {

        ValueWrapper<String> id = ValueWrapper.empty();
        ValueWrapper<String> name = ValueWrapper.empty();
        ValueWrapper<Set<ListOperation<WarehouseLocationDto>>> locations = ValueWrapper.empty();

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

        public Builder locations(final ValueWrapper<Set<ListOperation<WarehouseLocationDto>>> locations){
            this.locations = locations == null ? ValueWrapper.empty() : locations;
            return this;
        }


        public WarehouseDto build() {

            WarehouseDto instance = new WarehouseDto();
            instance.id = id;
            instance.name = name;
            instance.locations = locations;

            return instance;

        }

    }

    @ProjectionFor(WarehouseDto.class)
    public static class Projection extends DtoProjection<WarehouseDto> {

        public FieldConf id;
        public FieldConf name;
        public DtoListFieldConf<WarehouseLocationDto.Projection> locations;

        public Projection() {
        }

        public void applyProjectionTo(WarehouseDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.name = ProjectionUtils.getProjectedValue(dto.name, this.name);
            dto.locations = ProjectionUtils.getProjectedValue(dto.locations, this.locations);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return  Objects.equals(id, that.id)
                       && Objects.equals(name, that.name)
                       && Objects.equals(locations, that.locations);

        }

        @Override
        public int hashCode() {
            return Objects.hash(id,
                    name,
                    locations);
        }

        public static class Builder {

            FieldConf id;
            FieldConf name;
            DtoListFieldConf<WarehouseLocationDto.Projection> locations;

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

            public Builder locations(final DtoListFieldConf<WarehouseLocationDto.Projection> locations){
                this.locations = locations;
                return this;
            }

            public Builder locations(final Presence presence, final WarehouseLocationDto.Projection projection){
                locations = DtoListFieldConf.of(presence, projection);
                return this;
            }

            public WarehouseDto.Projection build() {

                WarehouseDto.Projection instance = new WarehouseDto.Projection();
                instance.id = id;
                instance.name = name;
                instance.locations = locations;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _id = "id";
        public static final String _name = "name";
        public static final String _locations = "locations";

    }

}