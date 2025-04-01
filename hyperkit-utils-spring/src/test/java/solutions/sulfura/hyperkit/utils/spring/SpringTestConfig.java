package solutions.sulfura.hyperkit.utils.spring;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import solutions.sulfura.hyperkit.utils.OptionValueWrapperAdapter;
import solutions.sulfura.hyperkit.dtos.ValueWrapperAdapter;

@SpringBootApplication
public class SpringTestConfig {

    @Bean
    public ValueWrapperAdapter<?> valueWrapperAdapter() {
        return new OptionValueWrapperAdapter();
    }

}