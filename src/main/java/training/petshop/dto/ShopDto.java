package training.petshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShopDto {

    @Schema(description = "Id of the shop", example = "12")
    private Long id;

    @Schema(description = "Name of the shop", example = "Cute animals shop")
    private String name;

    @Schema(description = "List of pets for sale")
    private List<PetDto> pets;

}
