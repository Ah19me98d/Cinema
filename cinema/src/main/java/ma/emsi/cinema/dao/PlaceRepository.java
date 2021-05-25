package ma.emsi.cinema.dao;


import ma.emsi.cinema.entities.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
@RepositoryRestResource
public interface PlaceRepository extends JpaRepository<Place, Long>{

}
