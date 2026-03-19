package solutions.sulfura.hyperkit.utils.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import solutions.sulfura.hyperkit.dsl.projections.CachedProjectionParser;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionAnnotationCache;
import solutions.sulfura.hyperkit.utils.serialization.DtoJacksonModule;
import solutions.sulfura.hyperkit.utils.serialization.alias.ProjectedDtoJacksonModule;
import solutions.sulfura.hyperkit.utils.serialization.value_wrapper.ValueWrapperJacksonModule;
import solutions.sulfura.hyperkit.utils.spring.jackson3.ProjectionAwareJacksonJsonConverter;
import tools.jackson.databind.json.JsonMapper;

public class SpringTestConfig implements WebMvcConfigurer {

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
    public ProjectionAwareJacksonJsonConverter projectionAwareJacksonConverter(JsonMapper jsonMapper,
                                                                               CachedProjectionParser cachedProjectionParser,
                                                                               ProjectionAnnotationCache projectionAnnotationCache) {
        return new ProjectionAwareJacksonJsonConverter(jsonMapper,
                cachedProjectionParser,
                projectionAnnotationCache);
    }

}
