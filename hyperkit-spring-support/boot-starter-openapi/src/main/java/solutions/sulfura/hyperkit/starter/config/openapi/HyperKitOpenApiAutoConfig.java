package solutions.sulfura.hyperkit.starter.config.openapi;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.webmvc.autoconfigure.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.spring.openapi.ProjectedSchemaBuilder;
import solutions.sulfura.hyperkit.utils.spring.openapi.ProjectionOpenApiCustomizer;
import solutions.sulfura.hyperkit.utils.spring.openapi.ValueWrapperModelConverter;
import solutions.sulfura.hyperkit.utils.spring.openapi.schemabuilder.stackprocessors.*;

import java.util.ArrayList;

@AutoConfiguration
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
public class HyperKitOpenApiAutoConfig implements WebMvcConfigurer {

    @Bean
    @ConditionalOnMissingBean
    public ProjectionOpenApiCustomizer projectionOpenApiCustomizer(RequestMappingHandlerMapping requestMappingHandlerMapping) {

        ArrayList<ProjectedSchemaBuilder.StackProcessor> stackProcessors = new ArrayList<>();

        stackProcessors.add(new SchemaReferenceStackProcessor());
        stackProcessors.add(
                new TypeReferenceStackProcessor(
                        ValueWrapper.class,
                        HttpEntity.class,
                        RequestEntity.class,
                        ResponseEntity.class
                )
        );
        stackProcessors.add(new ArrayStackProcessor());
        stackProcessors.add(new DtoProjectionStackProcessor());
        stackProcessors.add(new ListOperationStackProcessor());
        stackProcessors.add(new DefaultObjectStackProcessor());

        return new ProjectionOpenApiCustomizer(requestMappingHandlerMapping, stackProcessors);

    }

    @Bean
    public ValueWrapperModelConverter valueWrapperConverter() {
        return new ValueWrapperModelConverter();
    }

}
