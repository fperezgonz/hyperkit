package solutions.sulfura.hyperkit.utils.spring;

import org.springframework.context.annotation.Import;
import solutions.sulfura.hyperkit.utils.spring.projection_aware.FieldAliasSerializationTest;
import solutions.sulfura.hyperkit.utils.spring.projection_aware.ProjectionCacheTestConfig;

@Import({SpringTestConfig.class, ProjectionCacheTestConfig.class})
public class ProjectionAwareJacksonConverterTest extends FieldAliasSerializationTest {

}

