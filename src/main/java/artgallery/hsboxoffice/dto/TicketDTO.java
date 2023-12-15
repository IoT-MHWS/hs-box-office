package artgallery.hsboxoffice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketDTO {
    @NotNull
    private Long id;
    private String description;
    @DecimalMin(value = "0", message = "Price must be a positive number")
    private Integer price;
    private Long exhibition;
}
