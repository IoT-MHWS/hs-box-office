package artgallery.hsboxoffice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableReactiveFeignClients
public class HsBoxOfficeApplication {

    public static void main(String[] args) {
        SpringApplication.run(HsBoxOfficeApplication.class, args);
    }

}
