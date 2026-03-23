package solutions.sulfura.hyperkit.utils.spring;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import solutions.sulfura.hyperkit.dsl.projections.DtoProjectionSpec;
import solutions.sulfura.hyperkit.utils.spring.openapi.model.TestDto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@RestController
public class SchemaVerificationTestController {

    @DtoProjectionSpec(projectedClass = TestDto.class, namespace = "ComplexProjection", value = "id, nestedDto { id, nestedDto { id } }, nestedDtoList { id }")
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER, ElementType.TYPE_USE, ElementType.METHOD})
    @interface ComplexProjection {}

    @PostMapping("/schema-verification/complex-projection")
    public void complexProjectionOnRequestBody(@ComplexProjection @RequestBody TestDto dto) {
    }

    @DtoProjectionSpec(projectedClass = TestDto.class, namespace = "AliasedComplexProjection", value = "id as code, nestedDto as nested { id, nestedDto { id as deeplyNestedCode } }, nestedDtoList { id as itemCode }")
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER, ElementType.TYPE_USE, ElementType.METHOD})
    @interface AliasedComplexProjection {}

    @PostMapping("/schema-verification/aliased-complex-projection")
    public void aliasedComplexProjectionOnRequestBody(@AliasedComplexProjection @RequestBody TestDto dto) {
    }
}
