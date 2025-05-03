package solutions.sulfura.hyperkit.starter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.context.annotation.Configuration;
import solutions.sulfura.hyperkit.utils.serialization.DtoJacksonModule;
import solutions.sulfura.hyperkit.utils.serialization.value_wrapper.ValueWrapperJacksonModule;
import solutions.sulfura.hyperkit.utils.spring.HyperRepository;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.HyperMapper;
import solutions.sulfura.hyperkit.utils.spring.resolvers.DtoProjectionArgumentResolver;
import solutions.sulfura.hyperkit.utils.spring.resolvers.RsqlFilterArgumentResolver;
import solutions.sulfura.hyperkit.utils.spring.resolvers.SortArgumentResolver;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests to verify that all beans defined in {@link HyperKitAutoConfig} can be created without errors
 */
public class HyperKitAutoConfigTest {

    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
            .withUserConfiguration(TestConfig.class);

    @Test
    void shouldRegisterArgumentResolver() {
        contextRunner.run(context -> {
            assertNotNull(context);
            assertNotNull(context.getBean(HyperKitAutoConfig.class));
            ObjectMapper objectMapper = context.getBean(ObjectMapper.class);
            assertTrue(objectMapper.getRegisteredModuleIds().contains("solutions.sulfura.hyperkit.utils.serialization.value_wrapper.ValueWrapperJacksonModule"));
            assertTrue(objectMapper.getRegisteredModuleIds().contains("solutions.sulfura.hyperkit.utils.serialization.DtoJacksonModule"));
            assertNotNull(context.getBean(ObjectMapper.class));
            assertNotNull(context.getBean(DtoProjectionArgumentResolver.class));
            assertNotNull(context.getBean(SortArgumentResolver.class));
            assertNotNull(context.getBean(RsqlFilterArgumentResolver.class));
            assertNotNull(context.getBean(HyperRepository.class));
            assertNotNull(context.getBean(HyperMapper.class));
            assertNotNull(context.getBean(ValueWrapperJacksonModule.class));
            assertNotNull(context.getBean(DtoJacksonModule.class));
        });
    }

    @Configuration
    @EnableAutoConfiguration
    static class TestConfig {
    }

}
