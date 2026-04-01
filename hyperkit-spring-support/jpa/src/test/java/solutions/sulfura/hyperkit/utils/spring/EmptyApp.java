package solutions.sulfura.hyperkit.utils.spring;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "solutions.sulfura.hyperkit")
public class EmptyApp {
}
