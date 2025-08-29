package solutions.sulfura.hyperkit.utils.spring.hypermapper.nonentity;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;

public class PlainNestedDto implements Dto<PlainNested> {

    public ValueWrapper<Long> id = ValueWrapper.empty();
    public ValueWrapper<String> name = ValueWrapper.empty();
    public ValueWrapper<PlainNestedDto> child = ValueWrapper.empty();

    @Override
    public Class<PlainNested> getSourceClass() {
        return PlainNested.class;
    }
}
