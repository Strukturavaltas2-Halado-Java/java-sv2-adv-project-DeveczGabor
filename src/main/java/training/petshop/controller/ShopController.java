package training.petshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import training.petshop.dto.CreatePetCommand;
import training.petshop.dto.CreateShopCommand;
import training.petshop.dto.PetDto;
import training.petshop.dto.ShopDto;
import training.petshop.service.PetShopService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/petshops/shops")
@AllArgsConstructor
@Tag(name = "Management of shops")
public class ShopController {

    private PetShopService petShopService;

    @GetMapping
    @Operation(summary = "List all shop")
    public List<ShopDto> findAllShops() {
        return petShopService.listAllShop();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a shop by id")
    public ShopDto findShopById(@PathVariable("id") long id) {
        return petShopService.findShopById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a shop")
    @ApiResponse(responseCode = "201", description = "Shop has been created")
    @ApiResponse(responseCode = "400", description = "Bad request, shop cannot be created")
    public ShopDto createShop(@Valid @RequestBody CreateShopCommand createShopCommand) {
        return petShopService.createShop(createShopCommand);
    }

    @PostMapping("/{id}/pets")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a pet in a shop")
    @ApiResponse(responseCode = "201", description = "Pet has been created")
    @ApiResponse(responseCode = "400", description = "Bad request, pet cannot be created")
    public ShopDto addPetToShop(@Valid @PathVariable("id") long id, @Valid @RequestBody CreatePetCommand createPetCommand) {
        return petShopService.addPetToShop(id, createPetCommand);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a shop with pets")
    public ShopDto deleteShop(@PathVariable("id") long id) {
        return petShopService.deleteShop(id);
    }

    @DeleteMapping("/{shop-id}/pets/{pet-id}")
    @Operation(summary = "Sell a pet, remove from the list.")
    public PetDto sellPet(@PathVariable("shop-id") long shopId, @PathVariable("pet-id") long petId) {
        return petShopService.sellPet(shopId, petId);
    }
}
