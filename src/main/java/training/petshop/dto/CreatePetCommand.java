package training.petshop.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import training.petshop.model.Race;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreatePetCommand {

    @NotNull(message = "Pet name cannot be null!")
    @NotBlank(message = "Pet name cannot be empty!")
    @Schema(description = "Name of the pet", example = "Lóri")
    private String name;

    @NotNull(message = "Pet race cannot be null!")
    @Schema(description = "Race of the pet", example = "PARROT")
    private Race race;

    @PositiveOrZero
    @Schema(description = "The price of pet", example = "2000")
    private int price;
}
