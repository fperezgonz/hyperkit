package solutions.sulfura.hyperkit.utils.spring.hypermapper.entities;

import jakarta.persistence.*;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;
import solutions.sulfura.hyperkit.dtos.annotations.DtoProperty;

import java.io.Serializable;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "test_owning_one_to_many_child")
@Dto
public class OwningOneToManyChildEntity implements Serializable {

    @DtoProperty
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @DtoProperty
    @Column(name = "name")
    public String name;

}
