package solutions.sulfura.hyperkit.dtos;

import solutions.sulfura.hyperkit.dtos.annotations.Dto;
import solutions.sulfura.hyperkit.dtos.aux_classes.NestingClass.NestedClass;
import java.util.Date;

@Dto
public class SourceClassReferencesClassesWithSameName {
    public Date dateProperty;
    public java.sql.Date sqlDateProperty;
}