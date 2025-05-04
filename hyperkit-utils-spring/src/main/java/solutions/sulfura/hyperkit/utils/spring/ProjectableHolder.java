package solutions.sulfura.hyperkit.utils.spring;

import org.jspecify.annotations.NonNull;

import java.util.List;

/**
 * Utility interface to enable automatic application of projections on some processes
 */
public interface ProjectableHolder<P> {

    @NonNull
    List<P> getProjectables();

    void setProjectables(@NonNull List<P> projectables);

}
