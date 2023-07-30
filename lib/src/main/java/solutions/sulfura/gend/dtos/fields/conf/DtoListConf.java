package solutions.sulfura.gend.dtos.fields.conf;

/**
 * The purpose of this class is to specify the list operations allowed in the process related to this configuration
 * see {@link FieldConf}
 */
public class DtoListConf<T extends DtoConf<?>> extends DtoFieldConf<T> {

    public boolean allowInsert = false;
    public boolean allowUpdate = false;
    public boolean allowDelete = false;

    public static final class DtoListConfBuilder<T extends DtoConf<?>> {
        private T dtoConf;
        private boolean allowInsert;
        private boolean allowUpdate;
        private boolean allowDelete;
        private Presence presence;

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

        public DtoListConfBuilder<T> allowUpdate(boolean allowUpdate) {
            this.allowUpdate = allowUpdate;
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
                    .allowUpdate(allowUpdate)
                    .allowDelete(allowDelete)
                    .presence(presence);
        }

        public DtoListConf<T> build() {
            DtoListConf<T> dtoListConf = new DtoListConf<>();
            dtoListConf.allowUpdate = this.allowUpdate;
            dtoListConf.dtoConf = this.dtoConf;
            dtoListConf.presence = this.presence;
            dtoListConf.allowInsert = this.allowInsert;
            dtoListConf.allowDelete = this.allowDelete;
            return dtoListConf;
        }
    }
}
