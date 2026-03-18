package solutions.sulfura.hyperkit.utils.spring;

import org.jspecify.annotations.NonNull;
import solutions.sulfura.hyperkit.dtos.Dto;

import java.util.List;

public class SingleDtoResponseBody<D extends Dto<?>> implements ProjectableHolder<D> {

    private D data;

    public SingleDtoResponseBody() {
    }

    @NonNull
    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }

    @Override
    @NonNull
    public List<D> listProjectables() {
        return List.of(data);
    }
}
