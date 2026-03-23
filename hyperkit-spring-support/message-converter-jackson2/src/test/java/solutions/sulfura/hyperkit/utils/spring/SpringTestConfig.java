package solutions.sulfura.hyperkit.utils.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import solutions.sulfura.hyperkit.dsl.projections.CachedProjectionParser;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionAnnotationCache;
import solutions.sulfura.hyperkit.utils.serialization.jackson2.DtoJacksonModule;
import solutions.sulfura.hyperkit.utils.serialization.jackson2.alias.ProjectedDtoJacksonModule;
import solutions.sulfura.hyperkit.utils.serialization.jackson2.value_wrapper.ValueWrapperJacksonModule;

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
    public ProjectionAwareJacksonConverter projectionAwareJacksonConverter(ObjectMapper objectMapper,
                                                                           CachedProjectionParser cachedProjectionParser,
                                                                           ProjectionAnnotationCache projectionAnnotationCache) {
        return new ProjectionAwareJacksonConverter(objectMapper,
                cachedProjectionParser,
                projectionAnnotationCache);
    }

}
