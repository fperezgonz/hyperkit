package solutions.sulfura.gend.dtos.conf.fields;

import solutions.sulfura.gend.dtos.conf.DtoConf;

/**
 * The purpose of this class is to define presence or absence of the field on processes that adhere to this configuration, such as database queries or request data
 * ¿How should it handle permissions? If a field is declared as mandatory but the context has no permissions over it, then the process should fail. If a field is declared as IGNORED, the field should be set with NONE and if the field is set with another value, its value should be ignored by the processes using this config and treat it as NONE
 * If a field is not defined as MANDATORY or IGNORED its presence or absence should be determined by the context permissions
 */
public class DtoFieldConf<T extends DtoConf<?>> extends FieldConf {

    public T dtoConf;


    public static final class DtoFieldConfBuilder<T extends DtoConf<?>> {
        private T dtoConf;
        private Presence presence;

        private DtoFieldConfBuilder() {
        }

        public static <T extends DtoConf<?>> DtoFieldConfBuilder<T> newInstance() {
            return new DtoFieldConfBuilder<>();
        }

        public DtoFieldConfBuilder<T> dtoConf(T dtoConf) {
            this.dtoConf = dtoConf;
            return this;
        }

        public DtoFieldConfBuilder<T> presence(Presence presence) {
            this.presence = presence;
            return this;
        }

        public DtoFieldConf<T> build() {
            DtoFieldConf<T> dtoFieldConf = new DtoFieldConf<>();
            dtoFieldConf.dtoConf = this.dtoConf;
            dtoFieldConf.presence = this.presence;
            return dtoFieldConf;
        }
    }
}
