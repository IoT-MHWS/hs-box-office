package artgallery.hsboxoffice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table("ticket")
public class TicketEntity {
    @Id
    @Column("id")
    private Long id;

    @Column("description")
    private String description;

    @Column("price")
    private Integer price;

    @Column("exhibition")
    private Long exhibition;

}
