package training.petshop.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import training.petshop.dto.*;
import training.petshop.exception.PetNotFoundException;
import training.petshop.exception.PetNotFoundInTheShopException;
import training.petshop.exception.ShopNotFoundException;
import training.petshop.model.Pet;
import training.petshop.model.Race;
import training.petshop.model.Shop;
import training.petshop.repository.PetRepository;
import training.petshop.repository.ShopRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetShopService {

    private PetRepository petRepository;
    private ShopRepository shopRepository;
    private ModelMapper modelMapper;

    public PetShopService(PetRepository petRepository, ShopRepository shopRepository, ModelMapper modelMapper) {
        this.petRepository = petRepository;
        this.shopRepository = shopRepository;
        this.modelMapper = modelMapper;
    }

    public ShopDto createShop(CreateShopCommand createShopCommand) {
        Shop shop = shopRepository.save(new Shop(createShopCommand.getName()));
        return shopToShopDto(shop);
    }

    public List<ShopDto> listAllShop() {
        List<Shop> shops = shopRepository.findAll();
        return shops.stream().map(this::shopToShopDto).toList();
    }

    public ShopDto findShopById(Long id) {
        Shop shop = shopRepository.findById(id).orElseThrow(() -> new ShopNotFoundException(id));
        return shopToShopDto(shop);
    }


    public ShopDto deleteShop(long id) {
        Shop shop = shopRepository.findById(id).orElseThrow(() -> new ShopNotFoundException(id));
        shopRepository.delete(shop);
        return modelMapper.map(shop, ShopDto.class);
    }

    @Transactional
    public ShopDto addPetToShop(long id, CreatePetCommand createPetCommand) {
        Shop shop = shopRepository.findById(id).orElseThrow(() -> new ShopNotFoundException(id));
        Pet pet = new Pet(createPetCommand.getName(), createPetCommand.getRace(), createPetCommand.getPrice());
        petRepository.save(pet);
        shop.addPet(pet);
        return shopToShopDto(shop);
    }

    public PetDto sellPet(long shopId, long petId) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new ShopNotFoundException(shopId));
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new PetNotFoundInTheShopException(petId));
        if (shop.getPets().isEmpty() || !shop.getPets().stream()
                .map(Pet::getId)
                .collect(Collectors.toSet())
                .contains(petId)) {
            throw new PetNotFoundInTheShopException(petId);
        }
        petRepository.delete(pet);
        return modelMapper.map(pet, PetDto.class);
    }

    public List<PetDto> listPets(Optional<Race> race, Optional<Integer> limit) {
        List<Pet> pets = petRepository.findPetsByRaceAndMaxPrice(race, limit);
        return pets.stream().map(p -> modelMapper.map(p, PetDto.class)).toList();
    }

    @Transactional
    public PetDto changePetPriceById(long id, UpdatePetCommand updatePetCommand) {
        Pet pet = petRepository.findById(id).orElseThrow(() -> new PetNotFoundException(id));
        pet.setPrice(updatePetCommand.getPrice());
        return modelMapper.map(pet, PetDto.class);
    }

    public PetDto findPetById(long id) {
        Pet pet = petRepository.findById(id).orElseThrow(() -> new PetNotFoundException(id));
        return modelMapper.map(pet, PetDto.class);
    }

    private ShopDto shopToShopDto(Shop shop) {
        List<PetDto> result = shop.getPets().stream().map(p -> modelMapper.map(p, PetDto.class)).toList();
        return new ShopDto(shop.getId(), shop.getName(), result);
    }
}