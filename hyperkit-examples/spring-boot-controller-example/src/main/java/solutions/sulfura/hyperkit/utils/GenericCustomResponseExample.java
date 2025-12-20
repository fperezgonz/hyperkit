package solutions.sulfura.hyperkit.utils;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.utils.spring.DtoListResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class GenericCustomResponseExample<D extends Dto<?>> extends DtoListResponseBody<D> {


    protected HashMap<String, Object> meta;
    protected List<ErrorData> errors;

    public GenericCustomResponseExample() {
    }

    public Optional<HashMap<String, Object>> getMeta() {
        if (meta == null) {
            return Optional.empty();
        }
        return Optional.of(meta);
    }

    public void setMeta(HashMap<String, Object> meta) {
        this.meta = meta;
    }

    public Optional<List<ErrorData>> getErrors() {
        if (errors == null) {
            return Optional.empty();
        }
        return Optional.of(errors);
    }

    public void setErrors(List<ErrorData> errors) {
        this.errors = errors;
    }

    public static class ErrorData {
        public String errorCode;
        public String message;

        public ErrorData() {
        }

        public static final class Builder<D> {
            private String errorCode;
            private String message;

            public Builder() {
            }

            public static <D> Builder<D> newInstance() {
                return new Builder<>();
            }

            public Builder<D> errorCode(String val) {
                errorCode = val;
                return this;
            }

            public Builder<D> message(String val) {
                message = val;
                return this;
            }

            public ErrorData build() {
                ErrorData instance = new ErrorData();
                instance.errorCode = errorCode;
                instance.message = message;
                return instance;
            }
        }
    }


    public static final class Builder<D extends Dto<?>> {
        private List<D> data;
        public HashMap<String, Object> meta = new HashMap<>();
        private final List<ErrorData> errors = new ArrayList<>();

        public Builder() {
        }

        public static <D extends Dto<?>> Builder<D> newInstance() {
            return new Builder<>();
        }

        public Builder<D> data(List<D> val) {
            data = val;
            return this;
        }

        public Builder<D> addMetaAttribute(String key, Object value) {
            meta.put(key, value);
            return this;
        }

        public Builder<D> errors(List<ErrorData> val) {
            errors.clear();
            errors.addAll(val);
            return this;
        }

        public Builder<D> errors(ErrorData... val) {

            errors.clear();

            for (ErrorData errorData : val) {
                errors.add(errorData);
            }

            return this;

        }

        public GenericCustomResponseExample<D> build() {
            GenericCustomResponseExample<D> instance = new GenericCustomResponseExample<>();
            instance.setData(data);
            if (!this.errors.isEmpty()) {
                instance.errors = this.errors;
            }
            if (!this.meta.isEmpty()) {
                instance.meta = this.meta;
            }
            return instance;
        }

        public Builder<D> addError(ErrorData errorData) {
            this.errors.add(errorData);
            return this;
        }
    }
}
