package solutions.sulfura.hyperkit.utils.spring.hypermapper.nonentity;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;

public class PlainRootDto implements Dto<PlainRoot> {

    public ValueWrapper<Long> id = ValueWrapper.empty();
    public ValueWrapper<String> title = ValueWrapper.empty();
    public ValueWrapper<PlainNestedDto> nested = ValueWrapper.empty();

    @Override
    public Class<PlainRoot> getSourceClass() {
        return PlainRoot.class;
    }
}
