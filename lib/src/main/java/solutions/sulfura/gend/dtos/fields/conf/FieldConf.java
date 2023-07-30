package solutions.sulfura.gend.dtos.fields.conf;

/**
 * The purpose of this class is to define presence or absence of the field on processes that adhere to this configuration, such as database queries or request data
 * ¿How should it handle permissions? If a field is declared as mandatory but the context has no permissions over it, then the process should fail. If a field is declared as IGNORED, the field should be set with NONE and if the field is set with another value, its value should be ignored by the processes using this config and treat it as NONE
 * If a field is not defined as MANDATORY or IGNORED its presence or absence should be determined by the context permissions
 */
public class FieldConf {

    public enum Presence {MANDATORY, IGNORED, OPTIONAL}
    public Presence presence = Presence.OPTIONAL;


}
