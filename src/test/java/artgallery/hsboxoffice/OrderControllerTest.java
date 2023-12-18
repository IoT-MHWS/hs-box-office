package artgallery.hsboxoffice;

import artgallery.hsboxoffice.dto.OrderCreateDTO;
import artgallery.hsboxoffice.dto.OrderDTO;
import artgallery.hsboxoffice.service.OrderService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
public class OrderControllerTest extends AuthorizedControllerTest {
  @Autowired
  private WebTestClient webTestClient;

  static private OrderCreateDTO orderCreateDTO;

  @BeforeAll
  static void createOrderDTO() {
    orderCreateDTO = new OrderCreateDTO();
    orderCreateDTO.setDate(LocalDateTime.of(2025, 10, 25, 0, 26));
    orderCreateDTO.setLogin("user");
  }

  @Test
  void testOrderCreation() throws Exception {
    String request = objectMapper.writeValueAsString(orderCreateDTO);

    String result = webTestClient.post()
      .uri("/api/v1/orders/")
      .bodyValue(request)
      .headers(AuthorizedControllerTest::setupAdminHeaders)
      .header("Content-Type", "application/json")
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus()
      .isCreated()
      .returnResult(String.class)
      .getResponseBody()
      .blockFirst();

    OrderDTO resultDTO = objectMapper.readValue(result, OrderDTO.class);

    assertAll(
      () -> assertEquals(orderCreateDTO.getDate(), resultDTO.getDate()),
      () -> assertEquals(orderCreateDTO.getLogin(), resultDTO.getLogin())
    );
  }

  @Test
  void testNonExistentOrderRetrieving() {
    webTestClient.get()
      .uri("/api/v1/orders/0")
      .headers(AuthorizedControllerTest::setupPublicHeaders)
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus()
      .isNotFound();
  }

  @Nested
  class CreatedOrderTest {
    @Autowired
    OrderService orderService;

    OrderDTO orderDTO;

    @BeforeEach
    public void createOrder() {
      orderDTO = orderService.createOrder(orderCreateDTO).block();
    }

    @Test
    void testOrderRetrieving() throws Exception {
      String result = webTestClient.get()
        .uri("/api/v1/orders/{id}", orderDTO.getId())
        .headers(AuthorizedControllerTest::setupPublicHeaders)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .returnResult(String.class)
        .getResponseBody()
        .blockFirst();

      OrderDTO resultDTO = objectMapper.readValue(result, OrderDTO.class);

      assertAll(
        () -> assertEquals(orderDTO.getId(), resultDTO.getId()),
        () -> assertEquals(orderDTO.getDate(), resultDTO.getDate()),
        () -> assertEquals(orderDTO.getLogin(), resultDTO.getLogin())
      );
    }

    @Test
    void testOrderUpdating() throws Exception {
      String request = objectMapper.writeValueAsString(orderDTO);

      String result = webTestClient.put()
        .uri("/api/v1/orders/{id}", orderDTO.getId())
        .bodyValue(request)
        .headers(AuthorizedControllerTest::setupAdminHeaders)
        .header("Content-Type", "application/json")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .returnResult(String.class)
        .getResponseBody()
        .blockFirst();

      OrderDTO resultDTO = objectMapper.readValue(result, OrderDTO.class);

      assertAll(
        () -> assertEquals(orderCreateDTO.getDate(), resultDTO.getDate()),
        () -> assertEquals(orderCreateDTO.getLogin(), resultDTO.getLogin())
      );
    }

    @Test
    void testOrdersListing() throws Exception {
      String result = webTestClient.get()
        .uri("/api/v1/orders/")
        .headers(AuthorizedControllerTest::setupPublicHeaders)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .returnResult(String.class)
        .getResponseBody()
        .blockFirst();

      OrderDTO[] results = objectMapper.readValue(result, OrderDTO[].class);

      assertNotEquals(0, results.length);

      assertAll(
        () -> assertEquals(orderDTO.getDate(), results[0].getDate()),
        () -> assertEquals(orderDTO.getLogin(), results[0].getLogin())
      );
    }

    @Test
    void testOrderDeleting() {
      webTestClient.delete()
        .uri("/api/v1/orders/{id}", orderDTO.getId())
        .headers(AuthorizedControllerTest::setupAdminHeaders)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isNoContent();
    }

    @Test
    void testPermissionDeniedOrderDeleting() {
      webTestClient.delete()
        .uri("/api/v1/orders/{id}", orderDTO.getId())
        .headers(AuthorizedControllerTest::setupPublicHeaders)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isEqualTo(403);
    }

    @AfterEach
    public void deleteOrder() {
      orderService.deleteOrder(orderDTO.getId()).block();
    }
  }
}
