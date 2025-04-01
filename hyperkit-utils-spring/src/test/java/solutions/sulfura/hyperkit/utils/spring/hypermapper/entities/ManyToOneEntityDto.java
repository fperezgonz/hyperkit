package solutions.sulfura.hyperkit.utils.spring.hypermapper.entities;

import io.vavr.control.Option;
import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;

@DtoFor(ManyToOneEntity.class)
public class ManyToOneEntityDto implements Dto<ManyToOneEntity>{

    public Option<Long> id = Option.none();
    public Option<String> name = Option.none();
    public Option<String> description = Option.none();
    public Option<OneToManyEntityDto> oneToManyEntity = Option.none();

    public ManyToOneEntityDto() {
    }

    public Class<ManyToOneEntity> getSourceClass() {
        return ManyToOneEntity.class;
    }

    public static class Builder{

        Option<Long> id = Option.none();
        Option<String> name = Option.none();
        Option<String> description = Option.none();
        Option<OneToManyEntityDto> oneToManyEntity = Option.none();

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

        public Builder oneToManyEntity(final Option<OneToManyEntityDto> oneToManyEntity){
            this.oneToManyEntity = oneToManyEntity == null ? Option.none() : oneToManyEntity;
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