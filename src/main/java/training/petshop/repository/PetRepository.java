package training.petshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import training.petshop.model.Pet;
import training.petshop.model.Race;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    @Query("select p from Pet p where (:race is null or p.race = :race) and (:limit is null or p.price <= :limit)" +
            " order by p.race, p.price")
    List<Pet> findPetsByRaceAndMaxPrice(Optional<Race> race, Optional<Integer> limit);

}
