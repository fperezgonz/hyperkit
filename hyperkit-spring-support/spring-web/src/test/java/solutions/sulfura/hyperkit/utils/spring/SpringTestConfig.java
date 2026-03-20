package solutions.sulfura.hyperkit.utils.spring;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import solutions.sulfura.hyperkit.dsl.projections.CachedProjectionParser;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionAnnotationCache;
import solutions.sulfura.hyperkit.utils.serialization.DtoJacksonModule;
import solutions.sulfura.hyperkit.utils.serialization.alias.ProjectedDtoJacksonModule;
import solutions.sulfura.hyperkit.utils.serialization.value_wrapper.ValueWrapperJacksonModule;
import solutions.sulfura.hyperkit.utils.spring.jackson3.ProjectionAwareJacksonJsonConverter;
import solutions.sulfura.hyperkit.utils.spring.resolvers.*;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Optional;

public class SpringTestConfig implements WebMvcConfigurer {

    CachedProjectionParser cachedProjectionParser = new CachedProjectionParser();
    ProjectionAnnotationCache projectionAnnotationCache = new ProjectionAnnotationCache();

    @Bean
    @ConditionalOnMissingBean
    DtoProjectionReturnArgumentResolver dtoProjectionReturnArgumentResolver(ProjectionAnnotationCache projectionAnnotationCache,
                                                                            CachedProjectionParser cachedProjectionParser) {
        return new DtoProjectionReturnArgumentResolver(Optional.ofNullable(projectionAnnotationCache), Optional.ofNullable(cachedProjectionParser));
    }

    @Bean
    SortConverter sortConverter() {
        return new SortConverter();
    }

    @Bean
    public JsonMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder.disable(
                MapperFeature.SORT_PROPERTIES_ALPHABETICALLY
        );
    }

    @Bean
    public ValueWrapperJacksonModule valueWrapperJacksonModule() {
        return new ValueWrapperJacksonModule();
    }

    @Bean
    DtoJacksonModule dtoJacksonModule() {
        return new DtoJacksonModule();
    }

    @Bean
    ProjectedDtoJacksonModule projectedDtoJacksonModule() {
        return new ProjectedDtoJacksonModule();
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
    ProjectionAnnotationCache projectionAnnotationCache() {
        return projectionAnnotationCache;
    }

    @Bean
    @ConditionalOnMissingBean
    CachedProjectionParser cachedProjectionParser() {
        return cachedProjectionParser;
    }

    @Bean
    @ConditionalOnMissingBean
    public DtoProjectionRequestBodyAdvice dtoProjectionRequestBodyAdvice(CachedProjectionParser optionalCachedProjectionParser,
                                                                         ProjectionAnnotationCache optionalProjectionAnnotationCache) {
        return new DtoProjectionRequestBodyAdvice(Optional.ofNullable(optionalCachedProjectionParser), Optional.ofNullable(optionalProjectionAnnotationCache));
    }

    @Bean
    @ConditionalOnMissingBean
    public DtoProjectionResponseBodyAdvice dtoProjectionResponseBodyAdvice(CachedProjectionParser optionalCachedProjectionParser,
                                                                           ProjectionAnnotationCache optionalProjectionAnnotationCache) {
        return new DtoProjectionResponseBodyAdvice(Optional.ofNullable(optionalCachedProjectionParser), Optional.ofNullable(optionalProjectionAnnotationCache));
    }

    @Bean
    public ProjectionAwareJacksonJsonConverter projectionAwareJacksonConverter(JsonMapper objectMapper,
                                                                               CachedProjectionParser cachedProjectionParser,
                                                                               ProjectionAnnotationCache projectionAnnotationCache) {
        return new ProjectionAwareJacksonJsonConverter(objectMapper,
                cachedProjectionParser,
                projectionAnnotationCache);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new SortConverter());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(this.dtoProjectionReturnArgumentResolver(projectionAnnotationCache(), cachedProjectionParser()));
        resolvers.add(this.sortArgumentResolver());
        resolvers.add(this.rsqlFilterArgumentResolver());
    }

}
