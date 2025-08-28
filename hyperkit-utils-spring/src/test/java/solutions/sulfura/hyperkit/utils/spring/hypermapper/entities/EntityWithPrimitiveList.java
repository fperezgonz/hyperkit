package solutions.sulfura.hyperkit.utils.spring.hypermapper.entities;

import jakarta.persistence.*;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;
import solutions.sulfura.hyperkit.dtos.annotations.DtoProperty;

import java.util.List;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "test_entity_with_primitive_list")
@Dto(destPackageName = "cloud.sulfura.time.r.generated.temp.dto")
public class EntityWithPrimitiveList {

    @DtoProperty
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public Long id;
    
    @DtoProperty
    @Column(name = "name")
    public String name;
    
    @DtoProperty
    public List<String> stringList;
    
    @DtoProperty
    public List<Integer> integerList;
}