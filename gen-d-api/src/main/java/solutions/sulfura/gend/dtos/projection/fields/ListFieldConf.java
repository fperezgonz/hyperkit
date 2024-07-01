package solutions.sulfura.gend.dtos.projection.fields;

/**
 * The purpose of this class is to specify the list operations allowed in the process related to this configuration
 * see {@link FieldConf}
 */
public class ListFieldConf extends FieldConf {

    public boolean allowInsert = false;
    public boolean allowDelete = false;

    public static ListFieldConf valueOf(Presence presence) {
        ListFieldConf fieldConf = new ListFieldConf();
        fieldConf.presence = presence;
        return fieldConf;
    }


    public static final class ListConfBuilder {
        private Presence presence = Presence.IGNORED;
        private boolean allowInsert;
        private boolean allowDelete;

        private ListConfBuilder() {
        }

        public static ListConfBuilder newInstance() {
            return new ListConfBuilder();
        }

        public static ListConfBuilder valueOf(Presence presence) {
            return ListConfBuilder.newInstance().presence(presence);
        }

        public ListConfBuilder presence(Presence presence) {
            this.presence = presence;
            return this;
        }

        public ListConfBuilder allowInsert(boolean allowInsert) {
            this.allowInsert = allowInsert;
            return this;
        }

        public ListConfBuilder allowDelete(boolean allowDelete) {
            this.allowDelete = allowDelete;
            return this;
        }

        public ListConfBuilder but() {
            return newInstance()
                    .presence(presence)
                    .allowInsert(allowInsert)
                    .allowDelete(allowDelete);
        }

        public ListFieldConf build() {
            ListFieldConf listConf = new ListFieldConf();
            listConf.allowInsert = this.allowInsert;
            listConf.presence = this.presence;
            listConf.allowDelete = this.allowDelete;
            return listConf;
        }
    }
}
