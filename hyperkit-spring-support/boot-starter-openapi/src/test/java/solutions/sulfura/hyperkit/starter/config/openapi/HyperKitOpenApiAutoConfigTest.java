package solutions.sulfura.hyperkit.starter.config.openapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.context.annotation.Configuration;
import solutions.sulfura.hyperkit.utils.spring.openapi.ProjectionOpenApiCustomizer;
import solutions.sulfura.hyperkit.utils.spring.openapi.ValueWrapperModelConverter;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests to verify that all beans defined in {@link HyperKitOpenApiAutoConfig} can be created without errors
 */
public class HyperKitOpenApiAutoConfigTest {

    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
            .withUserConfiguration(TestConfig.class);

    @Test
    void shouldRegisterArgumentResolver() {
        contextRunner.run(context -> {
            assertNotNull(context);
            assertNotNull(context.getBean(HyperKitOpenApiAutoConfig.class));
            assertNotNull(context.getBean(ProjectionOpenApiCustomizer.class));
            assertNotNull(context.getBean(ValueWrapperModelConverter.class));
        });
    }

    @Configuration
    @EnableAutoConfiguration
    static class TestConfig {
    }

}
