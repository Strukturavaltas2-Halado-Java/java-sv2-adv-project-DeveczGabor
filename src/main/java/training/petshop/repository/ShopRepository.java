package training.petshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import training.petshop.model.Shop;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
}
