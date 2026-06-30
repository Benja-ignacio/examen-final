package cl.duoc.reviews.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDTO {
    private Long id;
    private String username;
    private UUID destinationId;
    private String destinationName;   // viene de Destination Service
    private String destinationCountry; // viene de Destination Service
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;

}
