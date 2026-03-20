package solutions.sulfura.hyperkit.utils.spring.hypermapper.entities;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoListFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;

import java.util.Set;

@SuppressWarnings("unused")
@DtoFor(ManyToManyRightEntity.class)
public class ManyToManyRightEntityDto implements Dto<ManyToManyRightEntity> {

    public ValueWrapper<Long> id = ValueWrapper.empty();
    public ValueWrapper<String> label = ValueWrapper.empty();
    public ValueWrapper<Set<solutions.sulfura.hyperkit.dtos.ListOperation<ManyToManyLeftEntityDto>>> lefts = ValueWrapper.empty();

    public ManyToManyRightEntityDto() {
    }

    public Class<ManyToManyRightEntity> getSourceClass() {
        return ManyToManyRightEntity.class;
    }

    public static class Builder {
        ValueWrapper<Long> id = ValueWrapper.empty();
        ValueWrapper<String> label = ValueWrapper.empty();
        ValueWrapper<Set<solutions.sulfura.hyperkit.dtos.ListOperation<ManyToManyLeftEntityDto>>> lefts = ValueWrapper.empty();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder id(final ValueWrapper<Long> id) {
            this.id = id == null ? ValueWrapper.empty() : id;
            return this;
        }

        public Builder label(final ValueWrapper<String> label) {
            this.label = label == null ? ValueWrapper.empty() : label;
            return this;
        }

        public Builder lefts(final ValueWrapper<Set<solutions.sulfura.hyperkit.dtos.ListOperation<ManyToManyLeftEntityDto>>> lefts) {
            this.lefts = lefts == null ? ValueWrapper.empty() : lefts;
            return this;
        }

        public ManyToManyRightEntityDto build() {
            ManyToManyRightEntityDto dto = new ManyToManyRightEntityDto();
            dto.id = id;
            dto.label = label;
            dto.lefts = lefts;
            return dto;
        }
    }

    @ProjectionFor(ManyToManyRightEntityDto.class)
    public static class Projection extends DtoProjection<ManyToManyRightEntityDto> {
        public FieldConf id;
        public FieldConf label;
        public DtoListFieldConf<ManyToManyLeftEntityDto.Projection> lefts;

        public void applyProjectionTo(ManyToManyRightEntityDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.label = ProjectionUtils.getProjectedValue(dto.label, this.label);
            dto.lefts = ProjectionUtils.getProjectedValue(dto.lefts, this.lefts);
        }

        public static class Builder {
            FieldConf id;
            FieldConf label;
            DtoListFieldConf<ManyToManyLeftEntityDto.Projection> lefts;

            public static Builder newInstance() {
                return new Builder();
            }

            public Builder id(final FieldConf id) {
                this.id = id;
                return this;
            }

            public Builder id(final Presence p) {
                this.id = FieldConf.of(p);
                return this;
            }

            public Builder label(final FieldConf label) {
                this.label = label;
                return this;
            }

            public Builder label(final Presence p) {
                this.label = FieldConf.of(p);
                return this;
            }

            public Builder lefts(final DtoListFieldConf<ManyToManyLeftEntityDto.Projection> lefts) {
                this.lefts = lefts;
                return this;
            }

            public Builder lefts(final Presence presence, final ManyToManyLeftEntityDto.Projection projection) {
                this.lefts = DtoListFieldConf.of(presence, projection);
                return this;
            }

            public Projection build() {
                Projection p = new Projection();
                p.id = id;
                p.label = label;
                p.lefts = lefts;
                return p;
            }
        }
    }

    public static class DtoModel {
        public static final String _id = "id";
        public static final String _label = "label";
        public static final String _lefts = "lefts";
    }

}
