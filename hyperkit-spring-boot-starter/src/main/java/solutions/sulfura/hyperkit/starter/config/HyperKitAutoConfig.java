package solutions.sulfura.hyperkit.starter.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpEntity;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.serialization.DtoJacksonModule;
import solutions.sulfura.hyperkit.utils.serialization.ValueWrapperAdapterImpl;
import solutions.sulfura.hyperkit.utils.serialization.value_wrapper.ValueWrapperJacksonModule;
import solutions.sulfura.hyperkit.utils.spring.HyperRepository;
import solutions.sulfura.hyperkit.utils.spring.HyperRepositoryImpl;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.HyperMapper;
import solutions.sulfura.hyperkit.utils.spring.openapi.ProjectedSchemaBuilder;
import solutions.sulfura.hyperkit.utils.spring.openapi.ProjectionOpenApiCustomizer;
import solutions.sulfura.hyperkit.utils.spring.openapi.ValueWrapperModelConverter;
import solutions.sulfura.hyperkit.utils.spring.openapi.schemabuilder.stackprocessors.*;
import solutions.sulfura.hyperkit.utils.spring.resolvers.*;

import java.util.ArrayList;
import java.util.List;

@AutoConfiguration
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
public class HyperKitAutoConfig implements WebMvcConfigurer {

    @Bean
    @ConditionalOnMissingBean
    DtoProjectionReturnArgumentResolver dtoProjectionReturnArgumentResolver() {
        return new DtoProjectionReturnArgumentResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    DtoProjectionRequestBodyAdvice dtoProjectionRequestBodyAdvice() {
        return new DtoProjectionRequestBodyAdvice();
    }

    @Bean
    @ConditionalOnMissingBean
    DtoProjectionResponseBodyAdvice dtoProjectionResponseBodyAdvice() {
        return new DtoProjectionResponseBodyAdvice(dtoProjectionRequestBodyAdvice());
    }

    @Bean
    @ConditionalOnMissingBean
    SortConverter sortConverter() {
        return new SortConverter();
    }

    @Bean
    @ConditionalOnMissingBean
    RsqlFilterConverter rsqlFilterConverterConverter() {
        return new RsqlFilterConverter();
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
    HyperRepository<?> hyperRepository() {
        return new HyperRepositoryImpl<>();
    }

    @Bean
    @ConditionalOnMissingBean
    public HyperMapper<?> hyperMapper(HyperRepository<?> repository) {
        return new HyperMapper<>(repository);
    }

    @Bean
    @ConditionalOnMissingBean
    public ValueWrapperJacksonModule valueWrapperJacksonModule() {
        return new ValueWrapperJacksonModule(new ValueWrapperAdapterImpl());
    }

    @Bean
    @ConditionalOnMissingBean
    public DtoJacksonModule dtoJacksonModule() {
        return new DtoJacksonModule();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new SortConverter());
        registry.addConverter(new RsqlFilterConverter());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(this.dtoProjectionReturnArgumentResolver());
        resolvers.add(this.sortArgumentResolver());
        resolvers.add(this.rsqlFilterArgumentResolver());
    }

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
        stackProcessors.add(new DefaultObjectStackProcessor());

        return new ProjectionOpenApiCustomizer(requestMappingHandlerMapping, stackProcessors);

    }

    @Bean
    public ValueWrapperModelConverter valueWrapperConverter() {
        return new ValueWrapperModelConverter();
    }

}
