package artgallery.hsboxoffice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CustomServerConfiguration {

  @Bean
  public ReactiveAuthenticationManager reactiveAuthenticationManager() {
    return Mono::just;
  }

  @Bean
  public HttpStatus defaultStatus() {
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }
}
