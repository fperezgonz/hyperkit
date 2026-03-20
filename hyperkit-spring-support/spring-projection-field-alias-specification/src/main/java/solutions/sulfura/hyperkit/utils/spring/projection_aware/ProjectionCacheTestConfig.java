package solutions.sulfura.hyperkit.utils.spring.projection_aware;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import solutions.sulfura.hyperkit.dsl.projections.CachedProjectionParser;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionAnnotationCache;

public class ProjectionCacheTestConfig implements WebMvcConfigurer {

    @Bean
    @ConditionalOnMissingBean
    ProjectionAnnotationCache projectionAnnotationCache() {
        return new ProjectionAnnotationCache();
    }

    @Bean
    @ConditionalOnMissingBean
    CachedProjectionParser projectionCache() {
        return new CachedProjectionParser();
    }

}
