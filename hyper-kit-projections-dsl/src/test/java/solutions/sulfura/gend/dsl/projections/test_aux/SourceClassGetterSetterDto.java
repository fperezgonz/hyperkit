package solutions.sulfura.gend.dsl.projections.test_aux;

import io.vavr.control.Option;
import solutions.sulfura.gend.dsl.projections.test_aux.dto_sources.SourceClassGetterSetter;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.projection.DtoProjection;
import solutions.sulfura.gend.dtos.projection.DtoProjectionException;
import solutions.sulfura.gend.dtos.projection.ProjectionFor;
import solutions.sulfura.gend.dtos.projection.ProjectionUtils;
import solutions.sulfura.gend.dtos.projection.fields.FieldConf;

@DtoFor(SourceClassGetterSetter.class)
public class SourceClassGetterSetterDto implements Dto<SourceClassGetterSetter>{

    public Option<String> stringPropertyWithGetter = Option.none();
    public Option<String> stringPropertyWithSetter = Option.none();
    public Option<String> stringPropertyWithGetterAndSetter = Option.none();

    public SourceClassGetterSetterDto(){}

    public Class<SourceClassGetterSetter> getSourceClass() {
        return SourceClassGetterSetter.class;
    }


    @ProjectionFor(SourceClassGetterSetterDto.class)
    public static class Projection extends DtoProjection<SourceClassGetterSetterDto>{

        public FieldConf stringPropertyWithGetter;
        public FieldConf stringPropertyWithSetter;
        public FieldConf stringPropertyWithGetterAndSetter;

        public void applyProjectionTo(SourceClassGetterSetterDto dto) throws DtoProjectionException {
            dto.stringPropertyWithGetter = ProjectionUtils.getProjectedValue(dto.stringPropertyWithGetter, this.stringPropertyWithGetter);
            dto.stringPropertyWithSetter = ProjectionUtils.getProjectedValue(dto.stringPropertyWithSetter, this.stringPropertyWithSetter);
            dto.stringPropertyWithGetterAndSetter = ProjectionUtils.getProjectedValue(dto.stringPropertyWithGetterAndSetter, this.stringPropertyWithGetterAndSetter);
        }

        public Projection(){}

    }

}