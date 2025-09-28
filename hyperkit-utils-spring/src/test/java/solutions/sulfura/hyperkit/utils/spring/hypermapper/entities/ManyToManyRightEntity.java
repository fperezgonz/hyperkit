package solutions.sulfura.hyperkit.utils.spring.hypermapper.entities;

import jakarta.persistence.*;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;
import solutions.sulfura.hyperkit.dtos.annotations.DtoProperty;

import java.util.Set;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "test_many_to_many_right")
@Dto(destPackageName = "cloud.sulfura.time.r.generated.temp.dto")
public class ManyToManyRightEntity {

    @DtoProperty
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @DtoProperty
    @Column(name = "label")
    public String label;

    @DtoProperty
    @ManyToMany(mappedBy = "rights")
    public Set<ManyToManyLeftEntity> lefts;

}
