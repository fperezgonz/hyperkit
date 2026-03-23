package solutions.sulfura.hyperkit.utils.spring;

import org.jspecify.annotations.NonNull;
import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * A request body that contains a List of Dtos in a "data" field
 * This class implements the {@link ProjectableHolder} interface, which enables automatic application of projections
 */
public class SingleDtoRequestBody<D extends Dto<?>> implements ProjectableHolder<D> {

    private ValueWrapper<D> data = ValueWrapper.empty();

    public SingleDtoRequestBody() {
    }

    @NonNull
    public ValueWrapper<D> getData() {
        return data;
    }

    public void setData(D data) {
        this.data = ValueWrapper.of(data);
    }

    @Override
    @NonNull
    public List<D> listProjectables() {

        if (data.isEmpty()) {
            return new ArrayList<>();
        }

        return List.of(data.get());

    }

}
