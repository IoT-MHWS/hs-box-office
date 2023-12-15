package artgallery.hsboxoffice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthFilter implements WebFilter {
    private static final String HEADER_USER_ID = "X-User-Id";
    private static final String HEADER_USER_NAME = "X-User-Name";
    private static final String HEADER_USER_ROLES = "X-User-Authorities";

    private final ReactiveAuthenticationManager reactiveAuthenticationManager;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
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

        return Mono.just(authToken)
          .flatMap(auth -> reactiveAuthenticationManager.authenticate(auth))
          .flatMap(auth -> chain.filter(exchange)
              .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth)));
    }
}
