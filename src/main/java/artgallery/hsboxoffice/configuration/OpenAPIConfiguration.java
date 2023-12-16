package artgallery.hsboxoffice.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "APIs", version = "1.0", description = "Documentation APIs v1.0"),
        security = {
                @SecurityRequirement(name = "X-User-Id", scopes = {"read"}),
                @SecurityRequirement(name = "X-User-Name", scopes = {"read"}),
                @SecurityRequirement(name = "X-User-Authorities", scopes = {"read"})
        }
)
@SecurityScheme(
        name = "X-User-Id",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "X-User-Id"
)
@SecurityScheme(
        name = "X-User-Name",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "X-User-Name"
)
@SecurityScheme(
        name = "X-User-Authorities",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "X-User-Authorities"
)
@Configuration
public class OpenAPIConfiguration {
}
