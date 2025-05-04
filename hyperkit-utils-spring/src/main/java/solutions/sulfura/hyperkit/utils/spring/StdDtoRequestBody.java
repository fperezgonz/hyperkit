package solutions.sulfura.hyperkit.utils.spring;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jspecify.annotations.NonNull;
import solutions.sulfura.hyperkit.dtos.Dto;

import java.util.ArrayList;
import java.util.List;

/**
 * A request body that contains a List of Dtos in a "data" field
 * This class implements the {@link ProjectableHolder} interface, which enables automatic application of projections
 */
public class StdDtoRequestBody<D extends Dto<?>> implements ProjectableHolder<D> {

    private final List<D> data = new ArrayList<>();

    public StdDtoRequestBody() {
    }

    @NonNull
    public List<D> getData() {
        return data;
    }

    public void setData(List<D> data) {
        this.data.clear();
        this.data.addAll(data);
    }

    @Override
    @JsonIgnore
    @NonNull
    public List<D> getProjectables() {
        return List.copyOf(data);
    }

    @Override
    public void setProjectables(List<D> projectables) {
        this.data.clear();
        this.data.addAll(projectables);
    }

}
