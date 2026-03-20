package solutions.sulfura.hyperkit.utils.spring;

import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springframework.context.annotation.Bean;

public class SpringTestConfigOpenApi_3_0 {

    @Bean
    public SpringDocConfigProperties springDocConfigProperties() {
        SpringDocConfigProperties result = new SpringDocConfigProperties();
        result.getApiDocs().setVersion(SpringDocConfigProperties.ApiDocs.OpenApiVersion.OPENAPI_3_0);
        return result;
    }

}
