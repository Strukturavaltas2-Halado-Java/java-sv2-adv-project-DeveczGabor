package training.petshop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pet_name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Race race;

    private int price;

    @ManyToOne()
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    public Pet(String name, Race race, int price) {
        this.name = name;
        this.race = race;
        this.price = price;
    }
}
