package solutions.sulfura.hyperkit.utils.spring.hypermapper.entities;

import jakarta.persistence.*;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;
import solutions.sulfura.hyperkit.dtos.annotations.DtoProperty;

import java.util.Set;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "test_one_to_many")
@Dto
public class OneToManyEntity {

    @DtoProperty
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;
    @DtoProperty
    @Column(name = "name")
    public String name;
    @DtoProperty
    @Column(name = "description")
    public String description;
    @DtoProperty
    @OneToMany(mappedBy = "oneToManyEntity")
    public Set<ManyToOneEntity> manyToOneEntities;
    @DtoProperty
    @ManyToOne
    @JoinColumn(name = "companyId")
    public Company company;
}

