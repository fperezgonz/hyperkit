package solutions.sulfura.hyperkit.utils.spring.hypermapper.entities;

import jakarta.persistence.*;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;
import solutions.sulfura.hyperkit.dtos.annotations.DtoProperty;

import java.util.Set;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "test_owning_one_to_many")
@Dto
public class OwningOneToManyEntity {

    @DtoProperty
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @DtoProperty
    @Column(name = "name")
    public String name;

    @DtoProperty
    @OneToMany
    @JoinColumn(name = "owning_one_to_many_id")
    public Set<OwningOneToManyChildEntity> children;

}
