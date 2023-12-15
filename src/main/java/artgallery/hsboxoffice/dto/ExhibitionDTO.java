package artgallery.hsboxoffice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExhibitionDTO {
    private long id;
    @NotNull
    private String name;
    @NotNull
    private Date startDate;
    @NotNull
    private Date endDate;
    private Long galleryId;
}
