package artgallery.hsboxoffice.configuration;

import artgallery.hsboxoffice.configuration.CustomAuthFilter;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

  private final String[] WHITE_LIST_URLS = {
      "/api-docs",
      "/api-docs/**",
      "/swagger-ui",
      "/swagger-ui/**",
      "/webjars/**"
  };

  @Bean
  public SecurityWebFilterChain filterChain(ServerHttpSecurity http, ReactiveAuthenticationManager reactiveAuthenticationManager) {
    http
        .authorizeExchange(exchange -> exchange
            .pathMatchers(WHITE_LIST_URLS).permitAll()
            .anyExchange().authenticated()
        )
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .cors(ServerHttpSecurity.CorsSpec::disable)
        .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
        .logout(ServerHttpSecurity.LogoutSpec::disable)
        .addFilterAt(customAuthFilter(reactiveAuthenticationManager), SecurityWebFiltersOrder.AUTHENTICATION);


    return http.build();
  }

  @Bean
  public CustomAuthFilter customAuthFilter(ReactiveAuthenticationManager reactiveAuthenticationManager) {
    return new CustomAuthFilter(reactiveAuthenticationManager);
  }

    @Bean
    @Order(-2)
    public CustomWebExceptionHandler customExceptionHandler(WebProperties webProperties, ApplicationContext applicationContext, ServerCodecConfigurer configurer) {
        CustomWebExceptionHandler exceptionHandler = new CustomWebExceptionHandler(
                new DefaultErrorAttributes(), webProperties.getResources(), applicationContext, exceptionToStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR
        );
        exceptionHandler.setMessageWriters(configurer.getWriters());
        exceptionHandler.setMessageReaders(configurer.getReaders());
        return exceptionHandler;
    }

    @Bean
    public Map<Class<? extends Exception>, HttpStatus> exceptionToStatusCode() {
        return Map.of(
            ResponseStatusException.class, HttpStatus.NOT_FOUND
        );
    }

}
