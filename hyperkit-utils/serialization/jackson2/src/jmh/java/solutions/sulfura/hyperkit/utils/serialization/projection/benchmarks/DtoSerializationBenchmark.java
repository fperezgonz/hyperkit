package solutions.sulfura.hyperkit.utils.serialization.projection.benchmarks;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openjdk.jmh.annotations.*;
import solutions.sulfura.hyperkit.dsl.projections.FieldAliasUtils;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionDsl;
import solutions.sulfura.hyperkit.utils.serialization.DtoJacksonModule;
import solutions.sulfura.hyperkit.utils.serialization.alias.ProjectedDtoJacksonModule;
import solutions.sulfura.hyperkit.utils.serialization.alias.serialization.AliasBeanPropertyWriter;
import solutions.sulfura.hyperkit.utils.serialization.value_wrapper.ValueWrapperJacksonModule;
import solutions.sulfura.hyperkit.utils.test.model.dtos.AuthorizationDto;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static solutions.sulfura.hyperkit.utils.serialization.alias.serialization.AliasBeanPropertyWriter.HYPERKIT_PROJECTION_ATTR_KEY;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 2, time = 5)
@Measurement(iterations = 5, time = 5)
public class DtoSerializationBenchmark {

    private ObjectMapper objectMapper;
    private AuthorizationDto.Projection projection;
    private final String sourceJson = """
                {"role":{"perms":[{"operationType":"NONE","itemOperationType":"NONE","value":{"id":"Users$READ"}}]}}""";

    @Setup
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new DtoJacksonModule());
        objectMapper.registerModule(new ValueWrapperJacksonModule());
        objectMapper.registerModule(new ProjectedDtoJacksonModule());

        projection = ProjectionDsl.parse("role { actions as perms { id } }", AuthorizationDto.Projection.class);
    }

    @Benchmark
    public AuthorizationDto deserializeWithCache() throws IOException {
        AliasBeanPropertyWriter.FIELD_ALIAS_UTILS_INSTANCE = new FieldAliasUtils(true);
        return objectMapper.reader()
                .withAttribute(HYPERKIT_PROJECTION_ATTR_KEY, projection)
                .readValue(sourceJson, AuthorizationDto.class);
    }

    @Benchmark
    public AuthorizationDto deserializeWithoutCache() throws IOException {
        AliasBeanPropertyWriter.FIELD_ALIAS_UTILS_INSTANCE = new FieldAliasUtils(false);
        return objectMapper.reader()
                .withAttribute(HYPERKIT_PROJECTION_ATTR_KEY, projection)
                .readValue(sourceJson, AuthorizationDto.class);
    }
}
