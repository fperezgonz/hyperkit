package solutions.sulfura.hyperkit.utils.spring.hypermapper.entities;

import jakarta.persistence.*;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;
import solutions.sulfura.hyperkit.dtos.annotations.DtoProperty;

import java.io.Serializable;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "test_many_to_one")
@Dto
public class ManyToOneEntity implements Serializable {

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
    @ManyToOne
    @JoinColumn(name = "one_to_many_id")
    public OneToManyEntity oneToManyEntity;
    @DtoProperty
    @ManyToOne
    @JoinColumn(name = "companyId")
    public Company company;

}
