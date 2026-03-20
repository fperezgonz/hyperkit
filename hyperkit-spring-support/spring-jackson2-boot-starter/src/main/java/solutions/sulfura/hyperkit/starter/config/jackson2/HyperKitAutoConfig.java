package solutions.sulfura.hyperkit.starter.config.jackson2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.webmvc.autoconfigure.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import solutions.sulfura.hyperkit.dsl.projections.CachedProjectionParser;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionAnnotationCache;
import solutions.sulfura.hyperkit.utils.serialization.jackson2.DtoJacksonModule;
import solutions.sulfura.hyperkit.utils.serialization.jackson2.alias.ProjectedDtoJacksonModule;
import solutions.sulfura.hyperkit.utils.serialization.jackson2.value_wrapper.ValueWrapperJacksonModule;
import solutions.sulfura.hyperkit.utils.spring.ProjectionAwareJacksonConverter;

@AutoConfiguration
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
public class HyperKitAutoConfig implements WebMvcConfigurer {

    @Bean
    @ConditionalOnMissingBean
    public ValueWrapperJacksonModule valueWrapperJackson2Module() {
        return new ValueWrapperJacksonModule();
    }

    @Bean
    @ConditionalOnMissingBean
    public DtoJacksonModule dtoJackson2Module() {
        return new DtoJacksonModule();
    }

    @Bean
    public ProjectedDtoJacksonModule projectedDtoJackson2Module() {
        return new ProjectedDtoJacksonModule();
    }

    @Bean
    public ProjectionAwareJacksonConverter projectionAwareJackson2Converter(ObjectMapper objectMapper,
                                                                           CachedProjectionParser cachedProjectionParser,
                                                                           ProjectionAnnotationCache projectionAnnotationCache) {
        return new ProjectionAwareJacksonConverter(objectMapper, cachedProjectionParser, projectionAnnotationCache);
    }


}
