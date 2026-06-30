package cl.duoc.reviews.dto;

import java.util.UUID;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateRequestDTO {
    @NotNull
    private UUID destinationId;     // referencia a Destination Service (solo el ID)

    @NotNull
    @Max(5)
    @Min(0)
    private Integer rating;
        
    @Size(min = 5, max = 256)
    private String comment;


}
