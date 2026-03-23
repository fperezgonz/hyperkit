package solutions.sulfura.hyperkit.utils.spring.openapi.model;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.utils.spring.DtoListResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DtoResponseWithMultipleFields<T extends Dto<?>> extends DtoListResponseBody<T> {

    public List<ErrorData> errors = new ArrayList<>();
    // Object field with no properties
    public HashMap<String, Object> extensions = new HashMap<>();

    public static class ErrorData {
        public String id;
        public String message;
        public String code;
        public ErrorSource source;
    }

    public static class ErrorSource {
        public String id;
    }

}
