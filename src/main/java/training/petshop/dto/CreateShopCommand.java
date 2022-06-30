package training.petshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateShopCommand {

    @NotNull(message = "Shop name cannot be null!")
    @NotBlank(message = "Shop name cannot be empty!")
    @Schema(description = "Name of the shop", example = "Cute animals shop")
    private String name;
}
