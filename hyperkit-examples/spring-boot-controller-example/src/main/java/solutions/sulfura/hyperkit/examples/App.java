package solutions.sulfura.hyperkit.examples;

import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringDocUtils.getConfig().addRequestWrapperToIgnore(DtoProjection.class);
        new SpringApplicationBuilder(App.class).run(args);
    }

}
