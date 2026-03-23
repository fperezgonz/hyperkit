package solutions.sulfura.hyperkit.starter.config.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;
import solutions.sulfura.hyperkit.utils.spring.HyperRepository;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.HyperMapper;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests to verify that all beans defined in {@link HyperKitJpaAutoConfig} can be created without errors
 */
public class HyperKitJpaAutoConfigTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(TestConfig.class);

    @Test
    void shouldRegisterArgumentResolver() {
        contextRunner.run(context -> {
            assertNotNull(context);
            assertNotNull(context.getBean(HyperKitJpaAutoConfig.class));
            assertNotNull(context.getBean(HyperRepository.class));
            assertNotNull(context.getBean(HyperMapper.class));
        });
    }

    @Configuration
    @EnableAutoConfiguration
    static class TestConfig {
    }

}
