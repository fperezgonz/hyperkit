package solutions.sulfura.gend.dtos.conf.fields;

/**
 * The purpose of this class is to define presence or absence of the field on processes that adhere to this configuration, such as database queries or request data
 * If a field is IGNORED, the field should be set with NONE and if the field is set with any other value, its value should be ignored by the processes using this config and treat it as NONE
 * Null values will be treated as IGNORED
 * ¿How should it handle permissions?
 * - If a field is MANDATORY but the context has no permissions over it, then the process should fail.
 * - If a field is not MANDATORY or IGNORED its presence or absence should be determined by the context permissions
 */
public class FieldConf {

    public enum Presence {MANDATORY, IGNORED, OPTIONAL}
    public Presence presence = Presence.IGNORED;


    public static final class FieldConfBuilder {
        private Presence presence = Presence.IGNORED;

        private FieldConfBuilder() {
        }

        public static FieldConfBuilder newInstance() {
            return new FieldConfBuilder();
        }

        public FieldConfBuilder presence(Presence presence) {
            this.presence = presence;
            return this;
        }

        public FieldConf build() {
            FieldConf fieldConf = new FieldConf();
            fieldConf.presence = this.presence;
            return fieldConf;
        }
    }
}
