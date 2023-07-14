package solutions.sulfura.gend.dtos.fields.conf;

/**
 * The purpose of this class is to specify the list operations allowed in the process related to this configuration
 * see {@link FieldConf}
 */
public class ListConf extends FieldConf {
    boolean allowInsert = false;
    boolean allowUpdate = false;
    boolean allowDelete = false;
}
