package training.petshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.PositiveOrZero;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdatePetCommand {

    @PositiveOrZero(message = "Price can not be negative!")
    @Schema(description = "The new price of the pet", example = "35")
    private int price;

}
