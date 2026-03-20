package solutions.sulfura.hyperkit.dtos;

import solutions.sulfura.hyperkit.dtos.annotations.Dto;
import solutions.sulfura.hyperkit.dtos.aux_classes.NestingClass.NestedClass;

/**
 * This class is used as input for the DTO generator test
 */
@Dto
public class SourceClassWithReferenceToNestedClass {
    public NestedClass nestedClassReference;
}