package solutions.sulfura.gend.dtos;

import solutions.sulfura.gend.dtos.annotations.Dto;
import solutions.sulfura.gend.dtos.annotations.DtoProperty;

/**This class is used as input for the DTO generator test */
@Dto
public class SourceUpperCaseProperties {

    public String UPPERCASE_FIELD_PROPERTY;
    private String PRESERVE_UPPERCASE_GETTER_PROPERTY;
    private String UPPERCASE_GETTER_PROPERTY;

    @DtoProperty(preserveCase = true)
    public String getPRESERVE_UPPERCASE_GETTER_PROPERTY() {
        return UPPERCASE_GETTER_PROPERTY;
    }

    public String getUPPERCASE_GETTER_PROPERTY() {
        return UPPERCASE_GETTER_PROPERTY;
    }
}
