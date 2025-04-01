package solutions.sulfura.hyperkit.utils.spring;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import solutions.sulfura.hyperkit.utils.OptionValueWrapperAdapter;
import solutions.sulfura.hyperkit.utils.ValueWrapperAdapter;

@SpringBootApplication
public class SpringTestConfig {

    @Bean
    public ValueWrapperAdapter<?> valueWrapperAdapter() {
        return new OptionValueWrapperAdapter();
    }

    @Bean JavaTimeModule javaTimeModule() {
        return new JavaTimeModule();
    }

}