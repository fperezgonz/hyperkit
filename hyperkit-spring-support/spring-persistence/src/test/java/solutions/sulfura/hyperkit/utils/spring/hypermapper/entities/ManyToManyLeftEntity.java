package solutions.sulfura.hyperkit.utils.spring.hypermapper.entities;

import jakarta.persistence.*;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;
import solutions.sulfura.hyperkit.dtos.annotations.DtoProperty;

import java.util.Set;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "test_many_to_many_left")
@Dto(destPackageName = "cloud.sulfura.time.r.generated.temp.dto")
public class ManyToManyLeftEntity {

    @DtoProperty
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @DtoProperty
    @Column(name = "name")
    public String name;

    @DtoProperty
    @ManyToMany
    @JoinTable(name = "test_left_right_join",
            joinColumns = @JoinColumn(name = "left_id"),
            inverseJoinColumns = @JoinColumn(name = "right_id"))
    public Set<ManyToManyRightEntity> rights;

}
