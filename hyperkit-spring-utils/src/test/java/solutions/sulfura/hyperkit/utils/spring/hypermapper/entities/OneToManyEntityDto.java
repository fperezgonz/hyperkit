package solutions.sulfura.hyperkit.utils.spring.hypermapper.entities;

import io.vavr.control.Option;
import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ListOperation;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoListFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;

import java.util.Set;

@DtoFor(OneToManyEntity.class)
public class OneToManyEntityDto implements Dto<OneToManyEntity>{

    public Option<Long> id = Option.none();
    public Option<String> name = Option.none();
    public Option<String> description = Option.none();
    public Option<Set<ListOperation<ManyToOneEntityDto>>> manyToOneEntities = Option.none();

    public OneToManyEntityDto() {
    }

    public Class<OneToManyEntity> getSourceClass() {
        return OneToManyEntity.class;
    }

    public static class Builder{

        Option<Long> id = Option.none();
        Option<String> name = Option.none();
        Option<String> description = Option.none();
        Option<Set<ListOperation<ManyToOneEntityDto>>> manyToOneEntities = Option.none();

        public static  Builder newInstance(){
            return new Builder();
        }

        public Builder id(final Option<Long> id){
            this.id = id == null ? Option.none() : id;
            return this;
        }

        public Builder name(final Option<String> name){
            this.name = name == null ? Option.none() : name;
            return this;
        }

        public Builder description(final Option<String> description){
            this.description = description == null ? Option.none() : description;
            return this;
        }

        public Builder manyToOneEntities(final Option<Set<ListOperation<ManyToOneEntityDto>>> manyToOneEntities){
            this.manyToOneEntities = manyToOneEntities == null ? Option.none() : manyToOneEntities;
            return this;
        }


        public OneToManyEntityDto build(){

            OneToManyEntityDto instance = new OneToManyEntityDto();
            instance.id = id;
            instance.name = name;
            instance.description = description;
            instance.manyToOneEntities = manyToOneEntities;

            return instance;

        }

    }

    @ProjectionFor(OneToManyEntityDto.class)
    public static class Projection extends DtoProjection<OneToManyEntityDto> {

        public FieldConf id;
        public FieldConf name;
        public FieldConf description;
        public DtoListFieldConf<ManyToOneEntityDto.Projection> manyToOneEntities;

        public Projection() {
        }

        public void applyProjectionTo(OneToManyEntityDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.name = ProjectionUtils.getProjectedValue(dto.name, this.name);
            dto.description = ProjectionUtils.getProjectedValue(dto.description, this.description);
            dto.manyToOneEntities = ProjectionUtils.getProjectedValue(dto.manyToOneEntities, this.manyToOneEntities);
        }

        public static class Builder{

            FieldConf id;
            FieldConf name;
            FieldConf description;
            DtoListFieldConf<ManyToOneEntityDto.Projection> manyToOneEntities;

            public static  Builder newInstance(){
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

            public Builder description(final FieldConf description){
                this.description = description;
                return this;
            }

            public Builder description(final Presence presence){
                description = FieldConf.of(presence);
                return this;
            }

            public Builder manyToOneEntities(final DtoListFieldConf<ManyToOneEntityDto.Projection> manyToOneEntities){
                this.manyToOneEntities = manyToOneEntities;
                return this;
            }

            public Builder manyToOneEntities(final Presence presence, final ManyToOneEntityDto.Projection projection){
                manyToOneEntities = DtoListFieldConf.of(presence, projection);
                return this;
            }

            public Projection build(){

                Projection instance = new Projection();
                instance.id = id;
                instance.name = name;
                instance.description = description;
                instance.manyToOneEntities = manyToOneEntities;

                return instance;

            }

        }

    }

    public static class DtoModel{

        public static final String _id = "id";
        public static final String _name = "name";
        public static final String _description = "description";
        public static final String _manyToOneEntities = "manyToOneEntities";

    }

}