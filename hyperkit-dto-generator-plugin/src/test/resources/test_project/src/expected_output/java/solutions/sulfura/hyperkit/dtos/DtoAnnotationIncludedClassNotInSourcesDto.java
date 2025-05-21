package solutions.sulfura.hyperkit.dtos;

import solutions.sulfura.hyperkit.dtos.Dto;
import test_project.src.test_input_sources.java.solutions.sulfura.hyperkit.dtos.DtoAnnotationIncludedClassNotInSources;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;

@DtoFor(DtoAnnotationIncludedClassNotInSources.class)
public class DtoAnnotationIncludedClassNotInSourcesDto implements Dto<DtoAnnotationIncludedClassNotInSources> {


    public DtoAnnotationIncludedClassNotInSourcesDto() {
    }

    public Class<DtoAnnotationIncludedClassNotInSources> getSourceClass() {
        return DtoAnnotationIncludedClassNotInSources.class;
    }

    public static class Builder {


        public static Builder newInstance() {
            return new Builder();
        }


        public DtoAnnotationIncludedClassNotInSourcesDto build() {

            DtoAnnotationIncludedClassNotInSourcesDto instance = new DtoAnnotationIncludedClassNotInSourcesDto();

            return instance;

        }

    }

    @ProjectionFor(DtoAnnotationIncludedClassNotInSourcesDto.class)
    public static class Projection extends DtoProjection<DtoAnnotationIncludedClassNotInSourcesDto> {


        public Projection() {
        }

        public void applyProjectionTo(DtoAnnotationIncludedClassNotInSourcesDto dto) throws DtoProjectionException {
        }

        public static class Builder {


            public static Builder newInstance() {
                return new Builder();
            }

            public DtoAnnotationIncludedClassNotInSourcesDto.Projection build() {

                DtoAnnotationIncludedClassNotInSourcesDto.Projection instance = new DtoAnnotationIncludedClassNotInSourcesDto.Projection();

                return instance;

            }

        }

    }

    public static class DtoModel {


    }

}