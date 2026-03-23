package solutions.sulfura.hyperkit.starter.config.web;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.webmvc.autoconfigure.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import solutions.sulfura.hyperkit.dsl.projections.CachedProjectionParser;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionAnnotationCache;
import solutions.sulfura.hyperkit.utils.serialization.jackson3.DtoJacksonModule;
import solutions.sulfura.hyperkit.utils.serialization.jackson3.alias.ProjectedDtoJacksonModule;
import solutions.sulfura.hyperkit.utils.serialization.jackson3.value_wrapper.ValueWrapperJacksonModule;
import solutions.sulfura.hyperkit.utils.spring.jackson3.ProjectionAwareJacksonJsonConverter;
import solutions.sulfura.hyperkit.utils.spring.resolvers.*;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Optional;

@AutoConfiguration
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
public class HyperKitWebAutoConfig implements WebMvcConfigurer {

    CachedProjectionParser cachedProjectionParser = new CachedProjectionParser();
    ProjectionAnnotationCache projectionAnnotationCache = new ProjectionAnnotationCache();

    @Bean
    @ConditionalOnMissingBean
    DtoProjectionReturnArgumentResolver dtoProjectionReturnArgumentResolver(ProjectionAnnotationCache projectionAnnotationCache,
                                                                            CachedProjectionParser cachedProjectionParser) {
        return new DtoProjectionReturnArgumentResolver(Optional.ofNullable(projectionAnnotationCache), Optional.ofNullable(cachedProjectionParser));
    }

    @Bean
    @ConditionalOnMissingBean
    CachedProjectionParser cachedProjectionParser() {
        return cachedProjectionParser;
    }

    @Bean
    @ConditionalOnMissingBean
    ProjectionAnnotationCache projectionAnnotationCache() {
        return projectionAnnotationCache;
    }

    @Bean
    @ConditionalOnMissingBean
    DtoProjectionRequestBodyAdvice dtoProjectionRequestBodyAdvice(CachedProjectionParser cachedProjectionParser, ProjectionAnnotationCache projectionAnnotationCache) {
        return new DtoProjectionRequestBodyAdvice(Optional.ofNullable(cachedProjectionParser), Optional.ofNullable(projectionAnnotationCache));
    }

    @Bean
    @ConditionalOnMissingBean
    DtoProjectionResponseBodyAdvice dtoProjectionResponseBodyAdvice(CachedProjectionParser cachedProjectionParser, ProjectionAnnotationCache projectionAnnotationCache) {
        return new DtoProjectionResponseBodyAdvice(Optional.ofNullable(cachedProjectionParser), Optional.ofNullable(projectionAnnotationCache));
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
    public ValueWrapperJacksonModule valueWrapperJacksonModule() {
        return new ValueWrapperJacksonModule();
    }

    @Bean
    @ConditionalOnMissingBean
    public DtoJacksonModule dtoJacksonModule() {
        return new DtoJacksonModule();
    }

    @Bean
    @ConditionalOnMissingBean
    public ProjectedDtoJacksonModule projectedDtoJacksonModule() {
        return new ProjectedDtoJacksonModule();
    }

    @Bean
    @ConditionalOnMissingBean
    public ProjectionAwareJacksonJsonConverter projectionAwareJacksonConverter(JsonMapper objectMapper,
                                                                               CachedProjectionParser cachedProjectionParser,
                                                                               ProjectionAnnotationCache projectionAnnotationCache) {
        return new ProjectionAwareJacksonJsonConverter(objectMapper, cachedProjectionParser, projectionAnnotationCache);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new SortConverter());
        registry.addConverter(new RsqlFilterConverter());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(this.dtoProjectionReturnArgumentResolver(projectionAnnotationCache(), cachedProjectionParser()));
        resolvers.add(this.sortArgumentResolver());
        resolvers.add(this.rsqlFilterArgumentResolver());
    }

}
