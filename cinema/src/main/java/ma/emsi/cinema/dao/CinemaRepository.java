package ma.emsi.cinema.dao;

import ma.emsi.cinema.entities.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
@RepositoryRestResource
public interface CinemaRepository extends JpaRepository<Cinema, Long>{

}
