package solutions.sulfura.hyperkit.starter.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.webmvc.autoconfigure.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import solutions.sulfura.hyperkit.starter.config.jpa.HyperKitJpaAutoConfig;
import solutions.sulfura.hyperkit.starter.config.openapi.HyperKitOpenApiAutoConfig;
import solutions.sulfura.hyperkit.starter.config.web.HyperKitWebAutoConfig;

@AutoConfiguration
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
@Import({HyperKitWebAutoConfig.class, HyperKitJpaAutoConfig.class, HyperKitOpenApiAutoConfig.class})
public class HyperKitAutoConfig implements WebMvcConfigurer {

}
