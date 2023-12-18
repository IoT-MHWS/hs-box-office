package artgallery.hsboxoffice;

import artgallery.hsboxoffice.dto.ExhibitionDTO;
import artgallery.hsboxoffice.dto.TicketDTO;
import artgallery.hsboxoffice.feign.ExhibitionClient;
import artgallery.hsboxoffice.model.TicketEntity;
import artgallery.hsboxoffice.repository.TicketRepository;
import artgallery.hsboxoffice.service.TicketService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
@MockBean(ExhibitionClient.class)
public class TicketControllerTest extends AuthorizedControllerTest {
  @Autowired
  private WebTestClient webTestClient;
  static private TicketDTO ticketDTO;
  @Autowired
  ExhibitionClient exhibitionClient;

  protected static final long exhibitionId = 1;

  @BeforeAll
  static void createTicketDTO() {
    ticketDTO = new TicketDTO();
    ticketDTO.setDescription("ticket");
    ticketDTO.setPrice(1000000);
    ticketDTO.setExhibition(exhibitionId);
  }

  @BeforeEach
  void setupFeignMock() {
    ExhibitionDTO exhibitionDTO = new ExhibitionDTO();
    exhibitionDTO.setId(exhibitionId);
    exhibitionDTO.setStartDate(new Date());
    exhibitionDTO.setEndDate(new Date());
    exhibitionDTO.setName("exhibition");
    exhibitionDTO.setGalleryId(2L);

    Mockito.doReturn(Mono.just(exhibitionDTO))
      .when(exhibitionClient)
      .getExhibitionById(exhibitionId, userId, username, adminAuthorities);
  }

  @Test
  void testTicketCreation() throws Exception {
    String request = objectMapper.writeValueAsString(ticketDTO);

    String result = webTestClient.post()
      .uri("/api/v1/tickets/")
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

    TicketDTO resultDTO = objectMapper.readValue(result, TicketDTO.class);

    assertAll(
      () -> assertEquals(ticketDTO.getDescription(), resultDTO.getDescription()),
      () -> assertEquals(ticketDTO.getPrice(), resultDTO.getPrice()),
      () -> assertEquals(ticketDTO.getExhibition(), resultDTO.getExhibition())
    );
  }

  @Nested
  class CreatedTicketTest {
    @Autowired
    TicketService ticketService;
    @Autowired
    TicketRepository ticketRepository;

    @BeforeEach
    public void createTicket() {
      TicketEntity ticketEntity = new TicketEntity();
      ticketEntity.setDescription(ticketDTO.getDescription());
      ticketEntity.setPrice(ticketDTO.getPrice());
      ticketEntity.setExhibition(ticketDTO.getExhibition());
      ticketEntity = ticketRepository.save(ticketEntity).block();

      ticketDTO.setId(Objects.requireNonNull(ticketEntity).getId());
    }

    @Test
    void testTicketRetrieving() throws Exception {
      String result = webTestClient.get()
        .uri("/api/v1/tickets/{id}", ticketDTO.getId())
        .headers(AuthorizedControllerTest::setupPublicHeaders)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .returnResult(String.class)
        .getResponseBody()
        .blockFirst();

      TicketDTO resultDTO = objectMapper.readValue(result, TicketDTO.class);

      assertAll(
        () -> assertEquals(ticketDTO.getId(), resultDTO.getId()),
        () -> assertEquals(ticketDTO.getDescription(), resultDTO.getDescription()),
        () -> assertEquals(ticketDTO.getPrice(), resultDTO.getPrice()),
        () -> assertEquals(ticketDTO.getExhibition(), resultDTO.getExhibition())
      );
    }

    @Test
    void testTicketUpdating() throws Exception {
      String request = objectMapper.writeValueAsString(ticketDTO);

      String result = webTestClient.put()
        .uri("/api/v1/tickets/{id}", ticketDTO.getId())
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

      TicketDTO resultDTO = objectMapper.readValue(result, TicketDTO.class);

      assertAll(
        () -> assertEquals(ticketDTO.getId(), resultDTO.getId()),
        () -> assertEquals(ticketDTO.getDescription(), resultDTO.getDescription()),
        () -> assertEquals(ticketDTO.getPrice(), resultDTO.getPrice()),
        () -> assertEquals(ticketDTO.getExhibition(), resultDTO.getExhibition())
      );
    }

    @Test
    void testTicketsListing() throws Exception {
      String result = webTestClient.get()
        .uri("/api/v1/tickets/")
        .headers(AuthorizedControllerTest::setupPublicHeaders)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .returnResult(String.class)
        .getResponseBody()
        .blockFirst();

      TicketDTO[] results = objectMapper.readValue(result, TicketDTO[].class);

      assertNotEquals(0, results.length);

      assertAll(
        () -> assertEquals(ticketDTO.getDescription(), results[0].getDescription()),
        () -> assertEquals(ticketDTO.getPrice(), results[0].getPrice()),
        () -> assertEquals(ticketDTO.getExhibition(), results[0].getExhibition())
      );
    }

    @Test
    void testTicketDeleting() {
      webTestClient.delete()
        .uri("/api/v1/tickets/{id}", ticketDTO.getId())
        .headers(AuthorizedControllerTest::setupAdminHeaders)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isNoContent();
    }

    @AfterEach
    public void deleteTicket() {
      ticketService.deleteTicket(ticketDTO.getId()).block();
    }
  }
}
