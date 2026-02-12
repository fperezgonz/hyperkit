package solutions.sulfura.hyperkit.dtos.projection.fields;

import java.util.Objects;

/**
 * The purpose of this class is to define presence or absence of the field on processes that adhere to this configuration, such as database queries or request data
 * If a field is IGNORED, the field should be set with NONE and if the field is set with any other value, its value should be ignored by the processes using this config and treat it as NONE
 * Null values will be treated as IGNORED
 * How should it handle permissions?
 * - If a field is MANDATORY but the context has no permissions over it, then the process should fail.
 * - If a field is not MANDATORY or IGNORED its presence or absence should be determined by the context permissions
 */
public class FieldConf {

    public enum Presence {MANDATORY, IGNORED, OPTIONAL}

    protected Presence presence = Presence.IGNORED;
    protected String fieldAlias;

    public Presence getPresence() {
        return presence;
    }

    public String getFieldAlias() {
        return fieldAlias;
    }

    public static FieldConf of(Presence presence) {
        FieldConf fieldConf = new FieldConf();
        fieldConf.presence = presence;
        return fieldConf;
    }

    public static FieldConf of(Presence presence, String fieldAlias) {
        FieldConf fieldConf = new FieldConf();
        fieldConf.presence = presence;
        fieldConf.fieldAlias = fieldAlias;
        return fieldConf;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FieldConf fieldConf = (FieldConf) o;
        return presence == fieldConf.presence && Objects.equals(fieldAlias, fieldConf.fieldAlias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(presence, fieldAlias);
    }

    public static FieldConf valueOf(Presence presence) {
        return of(presence);
    }

    public static final class FieldConfBuilder {
        private Presence presence = Presence.IGNORED;
        private String fieldAlias;

        private FieldConfBuilder() {
        }

        public static FieldConfBuilder newInstance() {
            return new FieldConfBuilder();
        }

        public static FieldConfBuilder of(Presence presence) {
            return FieldConfBuilder.newInstance()
                    .presence(presence);
        }

        public static FieldConfBuilder of(Presence presence, String fieldAlias) {
            return FieldConfBuilder.newInstance()
                    .presence(presence)
                    .fieldAlias(fieldAlias);
        }

        public static FieldConfBuilder valueOf(Presence presence) {
            return of(presence);
        }

        public static FieldConfBuilder valueOf(Presence presence, String fieldAlias) {
            return of(presence, fieldAlias);
        }

        public FieldConfBuilder presence(Presence presence) {
            this.presence = presence;
            return this;
        }

        public FieldConfBuilder fieldAlias(String fieldAlias) {
            this.fieldAlias = fieldAlias;
            return this;
        }

        public FieldConf build() {
            FieldConf fieldConf = new FieldConf();
            fieldConf.presence = this.presence;
            fieldConf.fieldAlias = this.fieldAlias;
            return fieldConf;
        }
    }
}
