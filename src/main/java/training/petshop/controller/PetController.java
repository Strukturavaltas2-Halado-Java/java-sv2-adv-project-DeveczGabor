package training.petshop.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import training.petshop.dto.PetDto;
import training.petshop.dto.UpdatePetCommand;
import training.petshop.model.Race;
import training.petshop.service.PetShopService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/petshops/pets")
@AllArgsConstructor
@Tag(name = "Management of pets")
public class PetController {

    private PetShopService petShopService;

    @GetMapping()
    @Operation(summary = "List of pets, with optional filters(race, max price")
    public List<PetDto> getPets(@RequestParam Optional<Race> race, @RequestParam Optional<Integer> limit) {
        return petShopService.listPets(race, limit);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a pet by id")
    public PetDto findPetById(@PathVariable("id") long id) {
        return petShopService.findPetById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Change price of pet, cannot be negative")
    public PetDto changePrice(@PathVariable("id") long id, @Valid @RequestBody UpdatePetCommand updatePetCommand) {
        return petShopService.changePetPriceById(id, updatePetCommand);
    }
}
