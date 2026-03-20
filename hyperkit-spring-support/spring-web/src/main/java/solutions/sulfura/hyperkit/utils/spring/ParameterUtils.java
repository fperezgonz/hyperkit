package solutions.sulfura.hyperkit.utils.spring;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestParam;

public class ParameterUtils {

    public static String getParameterName(MethodParameter parameter) {

        String paramName = parameter.getParameterName();
        var requestParamAnn = parameter.getParameterAnnotation(RequestParam.class);

        if (requestParamAnn == null) {
            return paramName;
        }

        paramName = requestParamAnn.name();

        if (paramName.isEmpty()) {
            paramName = requestParamAnn.value();
        }

        return paramName;

    }

}