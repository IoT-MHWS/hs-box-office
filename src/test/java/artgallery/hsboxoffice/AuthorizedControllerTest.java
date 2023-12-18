package artgallery.hsboxoffice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;

@AutoConfigureMockMvc
@SpringBootTest
public abstract class AuthorizedControllerTest {
  protected static final String HEADER_USER_ID = "X-User-Id";
  protected static final String HEADER_USER_NAME = "X-User-Name";
  protected static final String HEADER_USER_AUTHORITIES = "X-User-Authorities";
  protected static final String userId = "1";
  protected static final String username = "user";
  protected static final String publicAuthorities = "";
  protected static final String adminAuthorities = "ROLE_ADMIN";

  static protected final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeAll
  static void setupMapper() {
    objectMapper.registerModule(new JavaTimeModule());
  }

  protected static void setupPublicHeaders(HttpHeaders httpHeaders) {
    httpHeaders.set(HEADER_USER_ID, userId);
    httpHeaders.set(HEADER_USER_NAME, username);
    httpHeaders.set(HEADER_USER_AUTHORITIES, publicAuthorities);
  }

  protected static void setupAdminHeaders(HttpHeaders httpHeaders) {
    httpHeaders.set(HEADER_USER_ID, userId);
    httpHeaders.set(HEADER_USER_NAME, username);
    httpHeaders.set(HEADER_USER_AUTHORITIES, adminAuthorities);
  }
}
