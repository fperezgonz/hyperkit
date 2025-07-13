package solutions.sulfura.hyperkit.utils.spring.hypermapper.entities;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;

@SuppressWarnings("unused")
@DtoFor(ManyToOneEntity.class)
public class ManyToOneEntityDto implements Dto<ManyToOneEntity>{

    public ValueWrapper<Long> id = ValueWrapper.empty();
    public ValueWrapper<String> name = ValueWrapper.empty();
    public ValueWrapper<String> description = ValueWrapper.empty();
    public ValueWrapper<OneToManyEntityDto> oneToManyEntity = ValueWrapper.empty();

    public ManyToOneEntityDto() {
    }

    public Class<ManyToOneEntity> getSourceClass() {
        return ManyToOneEntity.class;
    }

    public static class Builder{

        ValueWrapper<Long> id = ValueWrapper.empty();
        ValueWrapper<String> name = ValueWrapper.empty();
        ValueWrapper<String> description = ValueWrapper.empty();
        ValueWrapper<OneToManyEntityDto> oneToManyEntity = ValueWrapper.empty();

        public static  Builder newInstance(){
            return new Builder();
        }

        public Builder id(final ValueWrapper<Long> id){
            this.id = id == null ? ValueWrapper.empty() : id;
            return this;
        }

        public Builder name(final ValueWrapper<String> name){
            this.name = name == null ? ValueWrapper.empty() : name;
            return this;
        }

        public Builder description(final ValueWrapper<String> description){
            this.description = description == null ? ValueWrapper.empty() : description;
            return this;
        }

        public Builder oneToManyEntity(final ValueWrapper<OneToManyEntityDto> oneToManyEntity){
            this.oneToManyEntity = oneToManyEntity == null ? ValueWrapper.empty() : oneToManyEntity;
            return this;
        }


        public ManyToOneEntityDto build(){

            ManyToOneEntityDto instance = new ManyToOneEntityDto();
            instance.id = id;
            instance.name = name;
            instance.description = description;
            instance.oneToManyEntity = oneToManyEntity;

            return instance;

        }

    }

    @ProjectionFor(ManyToOneEntityDto.class)
    public static class Projection extends DtoProjection<ManyToOneEntityDto> {

        public FieldConf id;
        public FieldConf name;
        public FieldConf description;
        public DtoFieldConf<OneToManyEntityDto.Projection> oneToManyEntity;

        public Projection() {
        }

        public void applyProjectionTo(ManyToOneEntityDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.name = ProjectionUtils.getProjectedValue(dto.name, this.name);
            dto.description = ProjectionUtils.getProjectedValue(dto.description, this.description);
            dto.oneToManyEntity = ProjectionUtils.getProjectedValue(dto.oneToManyEntity, this.oneToManyEntity);
        }

        public static class Builder{

            FieldConf id;
            FieldConf name;
            FieldConf description;
            DtoFieldConf<OneToManyEntityDto.Projection> oneToManyEntity;

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

            public Builder oneToManyEntity(final DtoFieldConf<OneToManyEntityDto.Projection> oneToManyEntity){
                this.oneToManyEntity = oneToManyEntity;
                return this;
            }

            public Builder oneToManyEntity(final Presence presence, final OneToManyEntityDto.Projection projection){
                oneToManyEntity = DtoFieldConf.of(presence, projection);
                return this;
            }

            public Projection build(){

                Projection instance = new Projection();
                instance.id = id;
                instance.name = name;
                instance.description = description;
                instance.oneToManyEntity = oneToManyEntity;

                return instance;

            }

        }

    }

    public static class DtoModel{

        public static final String _id = "id";
        public static final String _name = "name";
        public static final String _description = "description";
        public static final String _oneToManyEntity = "oneToManyEntity";

    }

}