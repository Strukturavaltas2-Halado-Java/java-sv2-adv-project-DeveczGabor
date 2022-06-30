package training.petshop.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import training.petshop.model.Race;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PetDto {

    @Schema(description = "Id of the pet", example = "13")
    private Long id;

    @Schema(description = "Name of the pet", example = "Mirnixdirnix")
    private String name;

    @Schema(description = "Race of the pet", example = "Parrot")
    private Race race;

    @Schema(description = "The price of pet", example = "20")
    private int price;

}
