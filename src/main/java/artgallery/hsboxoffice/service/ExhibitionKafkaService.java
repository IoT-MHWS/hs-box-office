package artgallery.hsboxoffice.service;

import artgallery.hsboxoffice.dto.ExhibitionDeleteDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExhibitionKafkaService {
    private final TicketService ticketService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "delete-exhibition", groupId = "box-office")
    public void deleteTicketsByExhibition(String msg) throws JsonProcessingException{
        ExhibitionDeleteDTO dto = objectMapper.readValue(msg, ExhibitionDeleteDTO.class);
        log.info("exh from cms service: " + dto.getId());
        ticketService.deleteTicketsByExhibitionId(dto.getId()).block();
    }
}