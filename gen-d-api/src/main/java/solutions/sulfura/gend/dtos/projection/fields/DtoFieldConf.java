package solutions.sulfura.gend.dtos.projection.fields;

import solutions.sulfura.gend.dtos.projection.DtoProjection;

/**
 * The purpose of this class is to define presence or absence of the field on processes that adhere to this configuration, such as database queries or request data
 * ¿How should it handle permissions? If a field is declared as mandatory but the context has no permissions over it, then the process should fail. If a field is declared as IGNORED, the field should be set with NONE and if the field is set with another value, its value should be ignored by the processes using this config and treat it as NONE
 * If a field is not defined as MANDATORY or IGNORED its presence or absence should be determined by the context permissions
 */
public class DtoFieldConf<T extends DtoProjection<?>> extends FieldConf {

    public T dtoProjection;

    public static <T extends DtoProjection<?>> DtoFieldConf<T> of(Presence presence, T dtoProjection) {
        DtoFieldConf<T> fieldConf = new DtoFieldConf<>();
        fieldConf.presence = presence;
        fieldConf.dtoProjection = dtoProjection;
        return fieldConf;
    }

    public static <T extends DtoProjection<?>> DtoFieldConf<T> valueOf(Presence presence, T dtoProjection) {
        return of(presence, dtoProjection);
    }

    public static final class DtoFieldConfBuilder<T extends DtoProjection<?>> {
        private T dtoProjection;
        private Presence presence = Presence.IGNORED;

        private DtoFieldConfBuilder() {
        }

        public static <T extends DtoProjection<?>> DtoFieldConfBuilder<T> newInstance() {
            return new DtoFieldConfBuilder<>();
        }


        public static <T extends DtoProjection<?>> DtoFieldConf.DtoFieldConfBuilder<T> of(Presence presence, T elemConf) {
            return DtoFieldConf.DtoFieldConfBuilder.<T>newInstance()
                    .presence(presence)
                    .dtoProjection(elemConf);
        }

        public static <T extends DtoProjection<?>> DtoFieldConf.DtoFieldConfBuilder<T> valueOf(Presence presence, T elemConf) {
            return of(presence, elemConf);
        }

        public DtoFieldConfBuilder<T> dtoProjection(T dtoConf) {
            this.dtoProjection = dtoConf;
            return this;
        }

        public DtoFieldConfBuilder<T> presence(Presence presence) {
            this.presence = presence;
            return this;
        }

        public DtoFieldConf<T> build() {
            DtoFieldConf<T> dtoFieldConf = new DtoFieldConf<>();
            dtoFieldConf.dtoProjection = this.dtoProjection;
            dtoFieldConf.presence = this.presence;
            return dtoFieldConf;
        }
    }
}
