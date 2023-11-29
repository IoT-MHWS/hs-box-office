package artgallery.hsboxoffice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    @NotNull
    private long id;
    private LocalDateTime date;
    @NotNull(message = "Order must have user")
    private String login;
}
