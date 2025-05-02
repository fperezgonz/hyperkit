package solutions.sulfura.hyperkit.starter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import solutions.sulfura.hyperkit.utils.serialization.DtoJacksonModule;
import solutions.sulfura.hyperkit.utils.serialization.ValueWrapperAdapterImpl;
import solutions.sulfura.hyperkit.utils.serialization.value_wrapper.ValueWrapperJacksonModule;
import solutions.sulfura.hyperkit.utils.spring.HyperRepository;
import solutions.sulfura.hyperkit.utils.spring.HyperRepositoryImpl;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.HyperMapper;
import solutions.sulfura.hyperkit.utils.spring.resolvers.*;

import java.util.List;

@AutoConfiguration
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
@EnableWebMvc
public class HyperKitAutoConfig implements WebMvcConfigurer {

    private final ObjectProvider<ObjectMapper> objectMapperProvider;

    public HyperKitAutoConfig(ObjectProvider<ObjectMapper> objectMapperProvider) {
        this.objectMapperProvider = objectMapperProvider;
    }

    @Bean
    @ConditionalOnMissingBean
    DtoProjectionReturnArgumentResolver dtoProjectionReturnArgumentResolver() {
        return new DtoProjectionReturnArgumentResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    DtoProjectionArgumentResolver dtoProjectionArgumentResolver() {
        return new DtoProjectionArgumentResolver(objectMapperProvider.getObject());
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
    HyperRepository<?> hyperRepository() {
        return new HyperRepositoryImpl<>();
    }

    @Bean
    @ConditionalOnMissingBean
    public HyperMapper<?> hyperMapper(HyperRepository<?> repository) {
        return new HyperMapper<>(repository);
    }

    @Bean
    @ConditionalOnMissingBean
    public ValueWrapperJacksonModule valueWrapperJacksonModule() {
        return new ValueWrapperJacksonModule(new ValueWrapperAdapterImpl());
    }

    @Bean
    @ConditionalOnMissingBean
    public DtoJacksonModule dtoJacksonModule() {
        return new DtoJacksonModule();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new SortConverter());
        registry.addConverter(new RsqlFilterConverter());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(this.dtoProjectionReturnArgumentResolver());
        resolvers.add(this.dtoProjectionArgumentResolver());
        resolvers.add(this.sortArgumentResolver());
        resolvers.add(this.rsqlFilterArgumentResolver());
    }

}
