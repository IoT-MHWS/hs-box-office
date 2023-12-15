package artgallery.hsboxoffice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table("orders")
public class OrderEntity {
    @Id
    private long id;

    @Column("date")
    private LocalDateTime date;

    @Column("login")
    private String login;
}
