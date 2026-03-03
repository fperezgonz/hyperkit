package solutions.sulfura.hyperkit.utils.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionCache;

public class SpringTestConfigNoProjectionCache extends SpringTestConfig {

    @Bean
    @Primary
    public ProjectionCache projectionCache() {
        return null;
    }

}
