package solutions.sulfura.gend.dtos.fields.conf;

/**
 * The purpose of this class is to specify the list operations allowed in the process related to this configuration
 * see {@link FieldConf}
 */
public class ListConf extends FieldConf {

    public boolean allowInsert = false;
    public boolean allowUpdate = false;
    public boolean allowDelete = false;


    public static final class ListConfBuilder {
        private Presence presence;
        private boolean allowInsert;
        private boolean allowUpdate;
        private boolean allowDelete;

        private ListConfBuilder() {
        }

        public static ListConfBuilder newInstance() {
            return new ListConfBuilder();
        }

        public ListConfBuilder presence(Presence presence) {
            this.presence = presence;
            return this;
        }

        public ListConfBuilder allowInsert(boolean allowInsert) {
            this.allowInsert = allowInsert;
            return this;
        }

        public ListConfBuilder allowUpdate(boolean allowUpdate) {
            this.allowUpdate = allowUpdate;
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
                    .allowUpdate(allowUpdate)
                    .allowDelete(allowDelete);
        }

        public ListConf build() {
            ListConf listConf = new ListConf();
            listConf.allowInsert = this.allowInsert;
            listConf.presence = this.presence;
            listConf.allowUpdate = this.allowUpdate;
            listConf.allowDelete = this.allowDelete;
            return listConf;
        }
    }
}
