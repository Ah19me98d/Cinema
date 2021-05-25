package ma.emsi.cinema.dao;

import ma.emsi.cinema.entities.Salle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
@RepositoryRestResource
public interface SalleRepository extends JpaRepository<Salle, Long>{

}
