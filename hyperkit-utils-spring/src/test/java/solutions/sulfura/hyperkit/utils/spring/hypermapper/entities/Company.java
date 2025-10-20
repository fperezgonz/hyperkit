package solutions.sulfura.hyperkit.utils.spring.hypermapper.entities;

import jakarta.persistence.*;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "test_entity_company")
@Dto(destPackageName = "cloud.sulfura.time.r.generated.temp.dto")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
