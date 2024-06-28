package solutions.sulfura.gend.dtos.conf.fields;

import solutions.sulfura.gend.dtos.conf.DtoConf;

/**
 * The purpose of this class is to specify the list operations allowed in the process related to this configuration
 * see {@link FieldConf}
 */
public class DtoListFieldConf<T extends DtoConf<?>> extends DtoFieldConf<T> {

    public boolean allowInsert = false;
    public boolean allowDelete = false;

    public static final class DtoListConfBuilder<T extends DtoConf<?>> {
        private T dtoConf;
        private boolean allowInsert;
        private boolean allowDelete;
        private Presence presence = Presence.IGNORED;

        private DtoListConfBuilder() {
        }

        public static <T extends DtoConf<?>> DtoListConfBuilder<T> newInstance() {
            return new DtoListConfBuilder<>();
        }

        public DtoListConfBuilder<T> dtoConf(T dtoConf) {
            this.dtoConf = dtoConf;
            return this;
        }

        public DtoListConfBuilder<T> allowInsert(boolean allowInsert) {
            this.allowInsert = allowInsert;
            return this;
        }

        public DtoListConfBuilder<T> allowDelete(boolean allowDelete) {
            this.allowDelete = allowDelete;
            return this;
        }

        public DtoListConfBuilder<T> presence(Presence presence) {
            this.presence = presence;
            return this;
        }

        public DtoListConfBuilder<T> but() {
            return DtoListConfBuilder.<T>newInstance().dtoConf(dtoConf)
                    .allowInsert(allowInsert)
                    .allowDelete(allowDelete)
                    .presence(presence);
        }

        public DtoListFieldConf<T> build() {
            DtoListFieldConf<T> dtoListFieldConf = new DtoListFieldConf<>();
            dtoListFieldConf.dtoConf = this.dtoConf;
            dtoListFieldConf.presence = this.presence;
            dtoListFieldConf.allowInsert = this.allowInsert;
            dtoListFieldConf.allowDelete = this.allowDelete;
            return dtoListFieldConf;
        }
    }
}
