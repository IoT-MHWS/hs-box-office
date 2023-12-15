package artgallery.hsboxoffice.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.List;

@Component
public class AuthFilter extends AuthenticationWebFilter {
  private static final String HEADER_USER_ID = "X-User-Id";
  private static final String HEADER_USER_NAME = "X-User-Name";
  private static final String HEADER_USER_ROLES = "X-User-Authorities";

  public AuthFilter(ReactiveAuthenticationManager reactiveAuthenticationManager) {
    super(reactiveAuthenticationManager);
    this.setServerAuthenticationConverter(new CustomServerAuthenticationConverter());
  }

  public static class CustomServerAuthenticationConverter implements ServerAuthenticationConverter {

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {

      ServerHttpRequest currentRequest = exchange.getRequest();
      HttpHeaders headers = currentRequest.getHeaders();

      Long userId = null;
      String userName = null;

      try {
        userId = Long.parseLong(headers.getFirst(HEADER_USER_ID));
        userName = headers.getFirst(HEADER_USER_NAME);
      } catch (NumberFormatException ignored) {
      }

      List<GrantedAuthority> authorities = ServerUserDetails.extractAuthorities(headers.getFirst(HEADER_USER_ROLES));
      var userDetails = new ServerUserDetails(userId, userName, authorities);
      var authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

      return Mono.just(authToken);
    }
  }

}
