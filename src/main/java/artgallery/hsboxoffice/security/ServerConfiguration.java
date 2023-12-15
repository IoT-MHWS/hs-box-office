package artgallery.hsboxoffice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import reactor.core.publisher.Mono;

@Configuration
public class ServerConfiguration {

  @Bean
  public ReactiveAuthenticationManager reactiveAuthenticationManager() {
    return authentication -> {
      var principal = (ServerUserDetails) authentication.getPrincipal();
      if (principal == null || principal.getId() == null || principal.getUsername() == null
          || principal.getAuthorities() == null || principal.getAuthorities().size() == 0) {
        return Mono.error(new UsernameNotFoundException("credentials are not set properly"));
      }
      return Mono.just(authentication);
    };
  }

}
