package solutions.sulfura.hyperkit.utils.spring.resolvers;

import io.github.perplexhub.rsql.RSQLJPASupport;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.jpa.domain.Specification;

public class RsqlFilterConverter implements Converter<String, Specification<?>> {

    @Override
    public Specification<?> convert(String source) {
        if (source.isEmpty()) {
            return null;
        }
        return RSQLJPASupport.toSpecification(source, true);
    }

}
