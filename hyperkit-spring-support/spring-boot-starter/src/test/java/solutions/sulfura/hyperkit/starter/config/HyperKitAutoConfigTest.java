package solutions.sulfura.hyperkit.starter.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.context.annotation.Configuration;
import solutions.sulfura.hyperkit.dsl.projections.CachedProjectionParser;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionAnnotationCache;
import solutions.sulfura.hyperkit.utils.serialization.jackson3.DtoJacksonModule;
import solutions.sulfura.hyperkit.utils.serialization.jackson3.alias.ProjectedDtoJacksonModule;
import solutions.sulfura.hyperkit.utils.serialization.jackson3.value_wrapper.ValueWrapperJacksonModule;
import solutions.sulfura.hyperkit.utils.spring.HyperRepository;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.HyperMapper;
import solutions.sulfura.hyperkit.utils.spring.jackson3.ProjectionAwareJacksonJsonConverter;
import solutions.sulfura.hyperkit.utils.spring.openapi.ProjectionOpenApiCustomizer;
import solutions.sulfura.hyperkit.utils.spring.openapi.ValueWrapperModelConverter;
import solutions.sulfura.hyperkit.utils.spring.resolvers.DtoProjectionRequestBodyAdvice;
import solutions.sulfura.hyperkit.utils.spring.resolvers.DtoProjectionResponseBodyAdvice;
import solutions.sulfura.hyperkit.utils.spring.resolvers.RsqlFilterArgumentResolver;
import solutions.sulfura.hyperkit.utils.spring.resolvers.SortArgumentResolver;
import tools.jackson.databind.json.JsonMapper;

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
            JsonMapper objectMapper = context.getBean(JsonMapper.class);
            objectMapper.registeredModules().stream().forEach(module-> System.out.println(module.getRegistrationId()));
            assertTrue(objectMapper.registeredModules().stream()
                    .anyMatch(module->module.getRegistrationId().equals("solutions.sulfura.hyperkit.utils.serialization.jackson3.value_wrapper.ValueWrapperJacksonModule")));
            assertTrue(objectMapper.registeredModules().stream()
                    .anyMatch(module->module.getRegistrationId().equals("solutions.sulfura.hyperkit.utils.serialization.jackson3.DtoJacksonModule")));
            assertTrue(objectMapper.registeredModules().stream()
                    .anyMatch(module->module.getRegistrationId().equals("solutions.sulfura.hyperkit.utils.serialization.jackson3.alias.ProjectedDtoJacksonModule")));
            assertNotNull(context.getBean(JsonMapper.class));
            assertNotNull(context.getBean(CachedProjectionParser.class));
            assertNotNull(context.getBean(ProjectionAnnotationCache.class));
            assertNotNull(context.getBean(DtoProjectionRequestBodyAdvice.class));
            assertNotNull(context.getBean(DtoProjectionResponseBodyAdvice.class));
            assertNotNull(context.getBean(SortArgumentResolver.class));
            assertNotNull(context.getBean(RsqlFilterArgumentResolver.class));
            assertNotNull(context.getBean(HyperRepository.class));
            assertNotNull(context.getBean(HyperMapper.class));
            assertNotNull(context.getBean(ValueWrapperJacksonModule.class));
            assertNotNull(context.getBean(DtoJacksonModule.class));
            assertNotNull(context.getBean(ProjectedDtoJacksonModule.class));
            assertNotNull(context.getBean(ProjectionAwareJacksonJsonConverter.class));
            assertNotNull(context.getBean(ProjectionOpenApiCustomizer.class));
            assertNotNull(context.getBean(ValueWrapperModelConverter.class));
        });
    }

    @Configuration
    @EnableAutoConfiguration
    static class TestConfig {
    }

}
