package solutions.sulfura.gend.dtos.generics;

import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.generics.SingleGenericParamSourceClass;
import java.util.Set;

@DtoFor(SingleGenericParamSourceClass.class)
public class SingleGenericParamSourceClassDto<T> implements Dto<SingleGenericParamSourceClass>{

    public Option<T> genericPropertyWithSetter;
    public Option<T> genericProperty;
    public Option<T> genericPropertyWithGetter;
    public Option<Set<T>> nestedGenericProperty;

    public SingleGenericParamSourceClassDto(){}

    public static <T> Builder<T> builder(){
        return new Builder<>();
    }

    public static class Builder<T>{

        public Option<T> genericPropertyWithSetter;
        public Option<T> genericProperty;
        public Option<T> genericPropertyWithGetter;
        public Option<Set<T>> nestedGenericProperty;

        public Builder<T> genericPropertyWithSetter(Option<T> genericPropertyWithSetter){
            this.genericPropertyWithSetter = genericPropertyWithSetter;
            return this;
        }

        public Builder<T> genericProperty(Option<T> genericProperty){
            this.genericProperty = genericProperty;
            return this;
        }

        public Builder<T> genericPropertyWithGetter(Option<T> genericPropertyWithGetter){
            this.genericPropertyWithGetter = genericPropertyWithGetter;
            return this;
        }

        public Builder<T> nestedGenericProperty(Option<Set<T>> nestedGenericProperty){
            this.nestedGenericProperty = nestedGenericProperty;
            return this;
        }

        public SingleGenericParamSourceClassDto<T> build(){
            SingleGenericParamSourceClassDto instance = new SingleGenericParamSourceClassDto();
            instance.genericPropertyWithSetter = genericPropertyWithSetter;
            instance.genericProperty = genericProperty;
            instance.genericPropertyWithGetter = genericPropertyWithGetter;
            instance.nestedGenericProperty = nestedGenericProperty;
            return instance;
        }

    }

}