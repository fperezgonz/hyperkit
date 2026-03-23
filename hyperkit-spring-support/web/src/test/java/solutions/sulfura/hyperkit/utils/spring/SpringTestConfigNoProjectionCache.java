package solutions.sulfura.hyperkit.utils.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import solutions.sulfura.hyperkit.dsl.projections.CachedProjectionParser;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionAnnotationCache;

public class SpringTestConfigNoProjectionCache extends SpringTestConfig {

    @Bean
    @Primary
    public CachedProjectionParser projectionCache() {
        return null;
    }

    @Bean
    @Primary
    public ProjectionAnnotationCache projectionAnnotationCache() {
        return null;
    }

}
