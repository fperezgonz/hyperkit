package solutions.sulfura.hyperkit.utils.spring;

import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpEntity;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.serialization.ValueWrapperAdapterImpl;
import solutions.sulfura.hyperkit.utils.serialization.value_wrapper.ValueWrapperJacksonModule;
import solutions.sulfura.hyperkit.utils.spring.openapi.ProjectedSchemaBuilder;
import solutions.sulfura.hyperkit.utils.spring.openapi.ProjectionOpenApiCustomizer;
import solutions.sulfura.hyperkit.utils.spring.openapi.ValueWrapperModelConverter;
import solutions.sulfura.hyperkit.utils.spring.openapi.schemabuilder.stackprocessors.*;
import solutions.sulfura.hyperkit.utils.spring.resolvers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class SpringTestConfig implements WebMvcConfigurer {

    @Bean
    @ConditionalOnMissingBean
    DtoProjectionReturnArgumentResolver dtoProjectionReturnArgumentResolver() {
        return new DtoProjectionReturnArgumentResolver();
    }

    @Bean
    SortConverter sortConverter() {
        return new SortConverter();
    }

    @Bean
    public ValueWrapperJacksonModule valueWrapperJacksonModule() {
        return new ValueWrapperJacksonModule(new ValueWrapperAdapterImpl());
    }

    @Bean
    @ConditionalOnMissingBean
    SortArgumentResolver sortArgumentResolver() {
        return new SortArgumentResolver(sortConverter());
    }

    @Bean
    @ConditionalOnMissingBean
    RsqlFilterArgumentResolver rsqlFilterArgumentResolver() {
        return new RsqlFilterArgumentResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    public DtoProjectionRequestBodyAdvice dtoProjectionRequestBodyAdvice() {
        return new DtoProjectionRequestBodyAdvice();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new SortConverter());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(this.dtoProjectionReturnArgumentResolver());
        resolvers.add(this.sortArgumentResolver());
        resolvers.add(this.rsqlFilterArgumentResolver());
    }

    @Bean
    public ProjectionOpenApiCustomizer projectionOpenApiCustomizer(RequestMappingHandlerMapping requestMappingHandlerMapping) {

        ArrayList<ProjectedSchemaBuilder.StackProcessor> stackProcessors = new ArrayList<>();

        stackProcessors.add(new SchemaReferenceStackProcessor());
        stackProcessors.add(new ArrayStackProcessor());
        stackProcessors.add(new DtoProjectionStackProcessor());
        stackProcessors.add(
                new TypeReferenceStackProcessor(
                        ValueWrapper.class,
                        HttpEntity.class,
                        List.class,
                        Set.class
                )
        );
        stackProcessors.add(new DefaultObjectStackProcessor());

        return new ProjectionOpenApiCustomizer(requestMappingHandlerMapping, stackProcessors);

    }

    @Bean
    public ValueWrapperModelConverter valueWrapperConverter() {
        return new ValueWrapperModelConverter();
    }

//    @Bean
//    public ApplicationListener<ContextRefreshedEvent> modelConverterConfigurer() {
//        return event -> {
//            SpringDocConfigProperties properties = event.getApplicationContext().getBean(SpringDocConfigProperties.class);
//            configureModelConverters(properties);
//        };
//    }
//
//
//    public void configureModelConverters(SpringDocConfigProperties springDocConfigProperties) {
//        ModelConverters modelConverters = ModelConverters.getInstance(springDocConfigProperties.isOpenapi31());
//
//        var converters = new ArrayList<ModelConverter>();
//        converters.addAll(modelConverters.getConverters());
//
//        for (ModelConverter converter : converters) {
//            modelConverters.removeConverter(converter);
//        }
//
////        ProjectionSpecDetector projectionSpecDetector = new ProjectionSpecDetector();
//        HyperModelResolver hyperModelResolver;
//        if (springDocConfigProperties.isOpenapi31()) {
//            hyperModelResolver = new HyperModelResolver(Json31.mapper(), TypeNameResolver.std);
//        } else {
//            hyperModelResolver = new HyperModelResolver(Json.mapper(), TypeNameResolver.std);
//        }
////        hyperModelResolver.openapi31(true);
//
//        converters.removeIf(converter -> converter instanceof ModelResolver);
//
//        // To be able to apply projections correctly, it must be the last to resolve
//        converters.add(hyperModelResolver);
//        // To detect the annotations, it must always be the first to resolve

    /// /        converters.add(projectionSpecDetector);
//
//        for (int i = converters.size() - 1; i >= 0; i--) {
//            ModelConverter converter = converters.get(i);
//            modelConverters.addConverter(converter);
//        }
//
//    }
    @Bean
    public SpringDocConfigProperties springDocConfigProperties() {
        SpringDocConfigProperties result = new SpringDocConfigProperties();
        // Recursive object references are not working well with OpenApi 3.1 https://github.com/springdoc/springdoc-openapi/issues/3040
        result.getApiDocs().setVersion(SpringDocConfigProperties.ApiDocs.OpenApiVersion.OPENAPI_3_0);
        return result;
    }

}
