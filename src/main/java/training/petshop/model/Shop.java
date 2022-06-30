package training.petshop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "shops")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shop_name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.REMOVE)
    private List<Pet> pets = new ArrayList<>();

    public Shop(String name) {
        this.name = name;
    }

    public void addPet(Pet pet) {
        pet.setShop(this);
        pets.add(pet);
    }
}
