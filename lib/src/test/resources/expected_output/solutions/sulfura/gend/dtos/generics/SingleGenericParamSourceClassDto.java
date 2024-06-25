package solutions.sulfura.gend.dtos.generics;

import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.conf.DtoConf;
import solutions.sulfura.gend.dtos.conf.fields.FieldConf;
import solutions.sulfura.gend.dtos.conf.fields.ListFieldConf;
import io.vavr.control.Option;
import java.util.Set;
import java.util.HashSet;
import solutions.sulfura.gend.dtos.ListOperation;
import solutions.sulfura.gend.dtos.generics.SingleGenericParamSourceClass;

@DtoFor(SingleGenericParamSourceClass.class)
public class SingleGenericParamSourceClassDto<T> implements Dto<SingleGenericParamSourceClass<T>>{

    public Option<T> genericPropertyWithSetter = Option.some(null);
    public Option<T> genericProperty = Option.some(null);
    public Option<T> genericPropertyWithGetter = Option.some(null);
    public Set<ListOperation<T>> nestedGenericProperty = new HashSet<>();

    public SingleGenericParamSourceClassDto(){}

    public static class Builder<T>{

        public Option<T> genericPropertyWithSetter = Option.some(null);
        public Option<T> genericProperty = Option.some(null);
        public Option<T> genericPropertyWithGetter = Option.some(null);
        public Set<ListOperation<T>> nestedGenericProperty = new HashSet<>();

        public static <T> Builder<T> newInstance(){
            return new Builder<>();
        }

        public Builder<T> genericPropertyWithSetter(Option<T> genericPropertyWithSetter){
            this.genericPropertyWithSetter = genericPropertyWithSetter == null ? Option.some(null) : genericPropertyWithSetter;
            return this;
        }

        public Builder<T> genericProperty(Option<T> genericProperty){
            this.genericProperty = genericProperty == null ? Option.some(null) : genericProperty;
            return this;
        }

        public Builder<T> genericPropertyWithGetter(Option<T> genericPropertyWithGetter){
            this.genericPropertyWithGetter = genericPropertyWithGetter == null ? Option.some(null) : genericPropertyWithGetter;
            return this;
        }

        public Builder<T> nestedGenericProperty(Set<ListOperation<T>> nestedGenericProperty){
            this.nestedGenericProperty = nestedGenericProperty == null ? new HashSet<>() : nestedGenericProperty;
            return this;
        }

        public SingleGenericParamSourceClassDto<T> build(){
            SingleGenericParamSourceClassDto<T> instance = new SingleGenericParamSourceClassDto<>();
            instance.genericPropertyWithSetter = genericPropertyWithSetter;
            instance.genericProperty = genericProperty;
            instance.genericPropertyWithGetter = genericPropertyWithGetter;
            instance.nestedGenericProperty = nestedGenericProperty;
            return instance;
        }

    }

    public static class Conf<T> extends DtoConf<SingleGenericParamSourceClassDto<T>>{

        public FieldConf genericPropertyWithSetter;
        public FieldConf genericProperty;
        public FieldConf genericPropertyWithGetter;
        public ListFieldConf nestedGenericProperty;

        public Conf(){}

        public static class Builder<T>{

            public FieldConf genericPropertyWithSetter;
            public FieldConf genericProperty;
            public FieldConf genericPropertyWithGetter;
            public ListFieldConf nestedGenericProperty;

            public static <T> Builder<T> newInstance(){
                return new Builder<>();
            }

            public Builder<T> genericPropertyWithSetter(FieldConf genericPropertyWithSetter){
                this.genericPropertyWithSetter = genericPropertyWithSetter;
                return this;
            }

            public Builder<T> genericProperty(FieldConf genericProperty){
                this.genericProperty = genericProperty;
                return this;
            }

            public Builder<T> genericPropertyWithGetter(FieldConf genericPropertyWithGetter){
                this.genericPropertyWithGetter = genericPropertyWithGetter;
                return this;
            }

            public Builder<T> nestedGenericProperty(ListFieldConf nestedGenericProperty){
                this.nestedGenericProperty = nestedGenericProperty;
                return this;
            }

            public SingleGenericParamSourceClassDto.Conf<T> build(){
                SingleGenericParamSourceClassDto.Conf<T> instance = new SingleGenericParamSourceClassDto.Conf<>();
                instance.genericPropertyWithSetter = genericPropertyWithSetter;
                instance.genericProperty = genericProperty;
                instance.genericPropertyWithGetter = genericPropertyWithGetter;
                instance.nestedGenericProperty = nestedGenericProperty;
                return instance;
            }

        }


    }

}