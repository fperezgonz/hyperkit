package solutions.sulfura.hyperkit.starter.config.jpa;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import solutions.sulfura.hyperkit.utils.spring.HyperRepository;
import solutions.sulfura.hyperkit.utils.spring.HyperRepositoryImpl;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.HyperMapper;

@AutoConfiguration
public class HyperKitJpaAutoConfig {

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

}
