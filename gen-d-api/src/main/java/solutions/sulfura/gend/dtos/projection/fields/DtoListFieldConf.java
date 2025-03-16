package solutions.sulfura.gend.dtos.projection.fields;

import solutions.sulfura.gend.dtos.projection.DtoProjection;

/**
 * The purpose of this class is to specify the list operations allowed in the process related to this configuration
 * see {@link FieldConf}
 */
public class DtoListFieldConf<T extends DtoProjection<?>> extends DtoFieldConf<T> {

    public boolean allowInsert = false;
    public boolean allowDelete = false;

    public static <T extends DtoProjection<?>> DtoListFieldConf<T> of(Presence presence, T dtoProjection) {
        DtoListFieldConf<T> fieldConf = new DtoListFieldConf<>();
        fieldConf.presence = presence;
        fieldConf.dtoProjection = dtoProjection;
        return fieldConf;
    }

    public static <T extends DtoProjection<?>> DtoListFieldConf<T> valueOf(Presence presence, T dtoProjection) {
        return of(presence, dtoProjection);
    }

    public static final class DtoListConfBuilder<T extends DtoProjection<?>> {
        private T dtoProjection;
        private boolean allowInsert;
        private boolean allowDelete;
        private Presence presence = Presence.IGNORED;

        private DtoListConfBuilder() {
        }

        public static <T extends DtoProjection<?>> DtoListConfBuilder<T> of(Presence presence, T elemConf) {
            return DtoListConfBuilder.<T>newInstance()
                    .presence(presence)
                    .dtoProjection(elemConf);
        }

        public static <T extends DtoProjection<?>> DtoListConfBuilder<T> valueOf(Presence presence, T elemConf) {
            return of(presence, elemConf);
        }

        public static <T extends DtoProjection<?>> DtoListConfBuilder<T> newInstance() {
            return new DtoListConfBuilder<>();
        }

        public static FieldConfBuilder valueOf(Presence presence) {
            return FieldConfBuilder.newInstance().presence(presence);
        }

        public DtoListConfBuilder<T> dtoProjection(T dtoProjection) {
            this.dtoProjection = dtoProjection;
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
            return DtoListConfBuilder.<T>newInstance().dtoProjection(dtoProjection)
                    .allowInsert(allowInsert)
                    .allowDelete(allowDelete)
                    .presence(presence);
        }

        public DtoListFieldConf<T> build() {
            DtoListFieldConf<T> dtoListFieldConf = new DtoListFieldConf<>();
            dtoListFieldConf.dtoProjection = this.dtoProjection;
            dtoListFieldConf.presence = this.presence;
            dtoListFieldConf.allowInsert = this.allowInsert;
            dtoListFieldConf.allowDelete = this.allowDelete;
            return dtoListFieldConf;
        }
    }
}
