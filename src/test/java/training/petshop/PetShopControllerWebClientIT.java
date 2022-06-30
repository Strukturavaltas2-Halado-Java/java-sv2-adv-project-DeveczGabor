package training.petshop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.zalando.problem.Problem;
import training.petshop.dto.*;
import training.petshop.model.Race;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {"delete from pets", "delete from shops"})
class PetShopControllerWebClientIT {

    @Autowired
    WebTestClient webClient;


    ShopDto shopDto;
    PetDto dogDto;
    PetDto catDto;
    PetDto parrotDto;


    @BeforeEach
    void init() {
        ShopDto tmp = webClient
                .post()
                .uri("/api/petshops/shops")
                .bodyValue(new CreateShopCommand("First Shop"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ShopDto.class)
                .returnResult().getResponseBody();
        webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/api/petshops/shops/{id}/pets/").build(tmp.getId()))
                .bodyValue(new CreatePetCommand("Morzsi", Race.DOG, 2000))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PetDto.class)
                .returnResult().getResponseBody();
        webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/api/petshops/shops/{id}/pets/").build(tmp.getId()))
                .bodyValue(new CreatePetCommand("Sziamiau", Race.CAT, 1500))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PetDto.class)
                .returnResult().getResponseBody();
        webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/api/petshops/shops/{id}/pets/").build(tmp.getId()))
                .bodyValue(new CreatePetCommand("Lóri", Race.PARROT, 4000))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PetDto.class)
                .returnResult().getResponseBody();
        shopDto = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("api/petshops/shops/{id}").build(tmp.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ShopDto.class)
                .returnResult().getResponseBody();
        dogDto = shopDto.getPets().get(0);
        catDto = shopDto.getPets().get(1);
        parrotDto = shopDto.getPets().get(2);
    }


    @Test
    void testCreateShop() {
        webClient
                .post()
                .uri("/api/petshops/shops")
                .bodyValue(new CreateShopCommand("Little Pets Shop"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ShopDto.class)
                .value(shopDto -> assertThat(shopDto.getName().equals("Little Pet Shop")));
    }

    @Test
    void testFindShopById() {

        long id = shopDto.getId();

        ShopDto result = webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/api/petshops/shops/{id}").build(shopDto.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ShopDto.class)
                .returnResult().getResponseBody();

        assertThat(result).extracting(ShopDto::getId, ShopDto::getName).containsExactly(shopDto.getId(), "First Shop");
    }

    @Test
    void testFindShopByIdWithWrongId() {

        Problem p = webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/api/petshops/shops/{id}").build(Long.MAX_VALUE))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(Problem.class)
                .returnResult().getResponseBody();

        assertThat(p.getDetail()).isEqualTo("Shop not found with id: " + Long.MAX_VALUE);
    }

    @Test
    void testFindAllShops() {
        List<ShopDto> result = webClient
                .get()
                .uri("/api/petshops/shops/")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ShopDto.class)
                .returnResult().getResponseBody();

        assertThat(result).hasSize(1).extracting(ShopDto::getName, s -> s.getPets().size())
                .containsExactly(tuple("First Shop", shopDto.getPets().size()));
    }

    @Test
    void testDeleteShop() {
        ShopDto result = webClient
                .delete()
                .uri("/api/petshops/shops/{id}", shopDto.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ShopDto.class)
                .returnResult().getResponseBody();

        int numberOfShops = webClient
                .get()
                .uri("/api/petshops/shops/")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ShopDto.class)
                .returnResult().getResponseBody().size();


        assertAll(
                () -> assertThat(numberOfShops).isEqualTo(0),
                () -> assertThat(result).extracting(ShopDto::getId, ShopDto::getName).containsExactly(shopDto.getId(), shopDto.getName())
        );
    }

    @Test
    void testDeleteShopWithWrongId() {
        Problem p = webClient
                .delete()
                .uri(uriBuilder -> uriBuilder.path("/api/petshops/shops/{id}").build(Long.MAX_VALUE))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(Problem.class)
                .returnResult().getResponseBody();

        assertThat(p.getDetail()).isEqualTo("Shop not found with id: " + Long.MAX_VALUE);

    }

    @Test
    void testAddPetToShop() {
        ShopDto result = webClient
                .post()
                .uri("/api/petshops/shops/{id}/pets/", shopDto.getId())
                .bodyValue(new CreatePetCommand("Bolhás", Race.DOG, 100))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ShopDto.class)
                .returnResult().getResponseBody();

        assertThat(result).extracting(ShopDto::getId, s -> s.getPets().size())
                .containsExactly(shopDto.getId(), shopDto.getPets().size() + 1);
    }

    @Test
    void testSellPet() {
        PetDto result = webClient
                .delete()
                .uri(uriBuilder -> uriBuilder.path("/api/petshops/shops/{shopId}/pets/{petId}").build(shopDto.getId(), catDto.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PetDto.class)
                .returnResult().getResponseBody();
        int numberOfPets = webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/api/petshops/shops/{id}").build(shopDto.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ShopDto.class)
                .returnResult().getResponseBody().getPets().size();

        assertThat(numberOfPets).isEqualTo(shopDto.getPets().size() - 1);
        assertThat(result.getId()).isNotIn(shopDto.getPets().stream().map(PetDto::getName).toList());
    }

    @Test
    void testFindPetById() {
        PetDto result = webClient
                .get()
                .uri("/api/petshops/pets/{id}", catDto.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(PetDto.class)
                .returnResult().getResponseBody();

        assertThat(result).extracting(PetDto::getId, PetDto::getName).containsExactly(catDto.getId(), catDto.getName());
    }

    @Test
    void testFindPetByWrongId() {
        Problem p = webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/api/petshops/pets/{id}").build(Long.MAX_VALUE))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(Problem.class)
                .returnResult().getResponseBody();

        assertThat(p.getDetail()).isEqualTo("Pet not found with id: " + Long.MAX_VALUE);
    }

    @Test
    void testChangePrice() {

        PetDto result = webClient
                .put()
                .uri("/api/petshops/pets/{id}", catDto.getId())
                .bodyValue(new UpdatePetCommand(200))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PetDto.class)
                .returnResult().getResponseBody();

        assertThat(result).extracting(PetDto::getId, PetDto::getPrice).containsExactly(catDto.getId(), 200);
    }

    @Test
    void testSellPetWithAnotherShopId() {
        ShopDto tmp = webClient
                .post()
                .uri("/api/petshops/shops")
                .bodyValue(new CreateShopCommand("Second Shop"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ShopDto.class)
                .returnResult().getResponseBody();
        long shopId = tmp.getId();
        tmp = webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/api/petshops/shops/{id}/pets/").build(shopId))
                .bodyValue(new CreatePetCommand("Another Morzsi", Race.DOG, 2000))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ShopDto.class)
                .returnResult().getResponseBody();
        Long petId = tmp.getPets().get(0).getId();

        Problem p = webClient
                .delete()
                .uri("/api/petshops/shops/{id}/pets/{id}", tmp.getId(), dogDto.getId())
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(Problem.class)
                .returnResult().getResponseBody();

        assertThat(p.getDetail()).isEqualTo("Pet not found in the shop with id: " + dogDto.getId());
    }

    @Test
    void testListPets() {
        List<PetDto> result = webClient
                .get()
                .uri("/api/petshops/pets/")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PetDto.class)
                .returnResult().getResponseBody();

        assertThat(result).hasSize(3).extracting(PetDto::getName).contains("Morzsi", "Sziamiau", "Lóri");
    }

    @Test
    void testListPetsWithFilters() {
        ShopDto tmp = webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/api/petshops/shops/{id}/pets/").build(shopDto.getId()))
                .bodyValue(new CreatePetCommand("Another Morzsi", Race.DOG, 700))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ShopDto.class)
                .returnResult().getResponseBody();

        List<PetDto> result = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/petshops/pets")
                        .queryParam("race", Race.DOG)
                        .queryParam("limit", 1000)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PetDto.class)
                .returnResult().getResponseBody();

        assertThat(result).hasSize(1).extracting(PetDto::getName, PetDto::getPrice).containsExactly(tuple("Another Morzsi", 700));
    }
}

