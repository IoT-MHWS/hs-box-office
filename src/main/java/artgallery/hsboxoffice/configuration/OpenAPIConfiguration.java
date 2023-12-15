// package artgallery.hsboxoffice.configuration;

// import io.swagger.v3.oas.annotations.OpenAPIDefinition;
// import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
// import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
// import io.swagger.v3.oas.annotations.info.Info;
// import io.swagger.v3.oas.annotations.security.SecurityRequirement;
// import io.swagger.v3.oas.annotations.security.SecurityScheme;
// import org.springframework.context.annotation.Configuration;


// @Configuration
// @OpenAPIDefinition(
//         info = @Info(
//                 title = "hs-box-office",
//                 description = "HS box-office service",
//                 version = "0.0.1"
//         ),
//         security = {
//                 @SecurityRequirement(
//                         name = "bearerAuth"
//                 )
//         }
// )
// @SecurityScheme(
//         name = "bearerAuth",
//         description = "JWT auth description",
//         scheme = "bearer",
//         type = SecuritySchemeType.HTTP,
//         bearerFormat = "JWT",
//         in = SecuritySchemeIn.HEADER
// )
// public class OpenAPIConfiguration {
// }
