package solutions.sulfura.hyperkit.utils.spring.benchmarks;

import org.openjdk.jmh.annotations.*;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import solutions.sulfura.hyperkit.utils.spring.EmptyApp;
import solutions.sulfura.hyperkit.utils.spring.SpringTestConfig;
import solutions.sulfura.hyperkit.utils.spring.SpringTestConfigNoProjectionCache;
import solutions.sulfura.hyperkit.utils.spring.resolvers.TestController;

import java.util.concurrent.TimeUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("unused")
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 1, time = 2)
@Measurement(iterations = 3, time = 1)
public class SpringProjectionBenchmark {


    private final String requestBody = """
            {"data":[{"code":1,"name":"Test Dto","age":25}]}""";


    @Benchmark
    public void benchmarkResponseProjectionWithCache(DefaultState state) throws Exception {
        state.mockMvc.perform(get("/test/test-aliased-dtos/"))
                .andExpect(status().isOk());
    }

    @Benchmark
    public void benchmarkResponseProjectionWithoutCache(NoCacheState state) throws Exception {
        state.mockMvc.perform(get("/test/test-aliased-dtos/"))
                .andExpect(status().isOk());
    }

    @Benchmark
    public void benchmarkResponseNoProjection(DefaultState state) throws Exception {
        state.mockMvc.perform(get("/test/test-no-projection/"))
                .andExpect(status().isOk());
    }

    @Benchmark
    public void benchmarkRequestProjectionWithCache(DefaultState state) throws Exception {
        state.mockMvc.perform(post("/test/test-aliased-dtos/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Benchmark
    public void benchmarkRequestProjectionWithoutCache(NoCacheState state) throws Exception {
        state.mockMvc.perform(post("/test/test-aliased-dtos/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Benchmark
    public void benchmarkRequestNoProjection(DefaultState state) throws Exception {
        state.mockMvc.perform(post("/test/test-no-projection/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @State(Scope.Benchmark)
    public static class DefaultState {

        private ConfigurableApplicationContext context;
        private MockMvc mockMvc;

        @Setup
        public void setup() {
            context = SpringApplication.run(new Class[]{EmptyApp.class, SpringTestConfig.class, TestController.class}, new String[]{"--server.port=0"});
            mockMvc = MockMvcBuilders.webAppContextSetup((WebApplicationContext) context).build();
        }

        @TearDown
        public void tearDown() {
            context.close();
        }
    }

    @State(Scope.Benchmark)
    public static class NoCacheState {

        private ConfigurableApplicationContext context;
        private MockMvc mockMvc;

        @Setup
        public void setup() {
            context = SpringApplication.run(new Class[]{EmptyApp.class, SpringTestConfigNoProjectionCache.class, TestController.class}, new String[]{"--server.port=0"});
            mockMvc = MockMvcBuilders.webAppContextSetup((WebApplicationContext) context).build();
        }

        @TearDown
        public void tearDown() {
            context.close();
        }
    }


}
