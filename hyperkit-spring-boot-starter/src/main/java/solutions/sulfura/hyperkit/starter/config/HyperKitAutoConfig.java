package solutions.sulfura.hyperkit.starter.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import solutions.sulfura.hyperkit.utils.serialization.ValueWrapperAdapterImpl;
import solutions.sulfura.hyperkit.utils.spring.HyperRepository;
import solutions.sulfura.hyperkit.utils.spring.HyperRepositoryImpl;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.HyperMapper;
import solutions.sulfura.hyperkit.utils.spring.resolvers.DtoProjectionReturnArgumentResolver;
import solutions.sulfura.hyperkit.utils.spring.resolvers.RsqlFilterArgumentResolver;
import solutions.sulfura.hyperkit.utils.spring.resolvers.SortArgumentResolver;

import java.util.List;

@AutoConfiguration
public class HyperKitAutoConfig implements WebMvcConfigurer {

    @Bean
    @ConditionalOnMissingBean
    DtoProjectionReturnArgumentResolver dtoProjectionReturnArgumentResolver() {
        return new DtoProjectionReturnArgumentResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    SortArgumentResolver sortArgumentResolver() {
        return new SortArgumentResolver();
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

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(this.dtoProjectionReturnArgumentResolver());
        resolvers.add(this.sortArgumentResolver());
        resolvers.add(this.rsqlFilterArgumentResolver());
    }

}
