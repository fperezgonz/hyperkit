package solutions.sulfura.gend.dtos.generics;

import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.projection.DtoProjection;
import solutions.sulfura.gend.dtos.projection.fields.FieldConf;
import solutions.sulfura.gend.dtos.projection.fields.ListFieldConf;
import io.vavr.control.Option;
import java.util.Set;
import solutions.sulfura.gend.dtos.ListOperation;
import solutions.sulfura.gend.dtos.generics.SingleGenericParamSourceClass;

@DtoFor(SingleGenericParamSourceClass.class)
public class SingleGenericParamSourceClassDto<T> implements Dto<SingleGenericParamSourceClass<T>>{

    public Option<T> genericPropertyWithSetter = Option.none();
    public Option<T> genericProperty = Option.none();
    public Option<T> genericPropertyWithGetter = Option.none();
    public Option<Set<ListOperation<T>>> nestedGenericProperty = Option.none();

    public SingleGenericParamSourceClassDto(){}

    public Class<SingleGenericParamSourceClass> getSourceClass() {
        return SingleGenericParamSourceClass.class;
    }

    public static class Builder<T>{

        public Option<T> genericPropertyWithSetter = Option.none();
        public Option<T> genericProperty = Option.none();
        public Option<T> genericPropertyWithGetter = Option.none();
        public Option<Set<ListOperation<T>>> nestedGenericProperty = Option.none();

        public static <T> Builder<T> newInstance(){
            return new Builder<>();
        }

        public Builder<T> genericPropertyWithSetter(Option<T> genericPropertyWithSetter){
            this.genericPropertyWithSetter = genericPropertyWithSetter == null ? Option.none() : genericPropertyWithSetter;
            return this;
        }

        public Builder<T> genericProperty(Option<T> genericProperty){
            this.genericProperty = genericProperty == null ? Option.none() : genericProperty;
            return this;
        }

        public Builder<T> genericPropertyWithGetter(Option<T> genericPropertyWithGetter){
            this.genericPropertyWithGetter = genericPropertyWithGetter == null ? Option.none() : genericPropertyWithGetter;
            return this;
        }

        public Builder<T> nestedGenericProperty(Option<Set<ListOperation<T>>> nestedGenericProperty){
            this.nestedGenericProperty = nestedGenericProperty == null ? Option.none() : nestedGenericProperty;
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

    public static class Projection<T> extends DtoProjection<SingleGenericParamSourceClassDto<T>>{

        public FieldConf genericPropertyWithSetter;
        public FieldConf genericProperty;
        public FieldConf genericPropertyWithGetter;
        public ListFieldConf nestedGenericProperty;

        public Projection(){}

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

            public SingleGenericParamSourceClassDto.Projection<T> build(){
                SingleGenericParamSourceClassDto.Projection<T> instance = new SingleGenericParamSourceClassDto.Projection<>();
                instance.genericPropertyWithSetter = genericPropertyWithSetter;
                instance.genericProperty = genericProperty;
                instance.genericPropertyWithGetter = genericPropertyWithGetter;
                instance.nestedGenericProperty = nestedGenericProperty;
                return instance;
            }

        }


    }

    public static class DtoModel{

        public static final String _genericPropertyWithSetter = "genericPropertyWithSetter";
        public static final String _genericProperty = "genericProperty";
        public static final String _genericPropertyWithGetter = "genericPropertyWithGetter";
        public static final String _nestedGenericProperty = "nestedGenericProperty";

    }

}