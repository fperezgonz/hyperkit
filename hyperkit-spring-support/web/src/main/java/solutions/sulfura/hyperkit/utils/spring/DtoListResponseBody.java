package solutions.sulfura.hyperkit.utils.spring;

import org.jspecify.annotations.NonNull;
import solutions.sulfura.hyperkit.dtos.Dto;

import java.util.ArrayList;
import java.util.List;

/**
 * A response body that contains a List of Dtos in a "data" field
 * This class implements the {@link ProjectableHolder} interface, which enables automatic application of projections
 */
public class DtoListResponseBody<D extends Dto<?>> implements ProjectableHolder<D> {

    private final List<D> data = new ArrayList<>();

    public DtoListResponseBody() {
    }

    @NonNull
    public List<D> getData() {
        return data;
    }

    public void setData(List<D> data) {

        this.data.clear();

        if (data == null) {
            return;
        }

        this.data.addAll(data);

    }

    @Override
    @NonNull
    public List<D> listProjectables() {
        return List.copyOf(data);
    }

    public static class Builder<D extends Dto<?>> {

        private List<D> data = new ArrayList<>();

        public static <D extends Dto<?>> Builder<D> newInstance() {
            return new Builder<>();
        }

        public Builder<D> data(List<D> data) {
            this.data = data;
            return this;
        }

        public DtoListResponseBody<D> build() {
            DtoListResponseBody<D> instance = new DtoListResponseBody<>();
            instance.setData(data);
            return instance;
        }

    }

}
