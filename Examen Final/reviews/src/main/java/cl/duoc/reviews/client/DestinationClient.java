package cl.duoc.reviews.client;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import java.util.UUID;
import cl.duoc.reviews.dto.ApiResponse;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Qualifier;


import cl.duoc.reviews.dto.destination.DestinationResponseDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Service
@Getter
@Setter
@RequiredArgsConstructor
public class DestinationClient {

    @Qualifier("destinationWebClient")
    private final WebClient webClient;

        public DestinationResponseDTO getDestinationById(UUID destinationId, String token) {
        try {
            ApiResponse<DestinationResponseDTO> response = webClient.get()
                    .uri("/{id}", destinationId)
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToMono(new org.springframework.core.ParameterizedTypeReference<ApiResponse<DestinationResponseDTO>>() {})
                    .timeout(Duration.ofSeconds(3))
                    .block();

            if (response == null || response.getData() == null) {
                return null;
            }
            return response.getData();

        } catch (WebClientResponseException.NotFound ex) {
            return null; // destino no existe
        } catch (WebClientResponseException.Unauthorized ex) {
            throw new RuntimeException("Token inválido al consultar Destination Service");
        } catch (Exception ex) {
            throw new RuntimeException("Error al comunicarse con Destination Service: " + ex.getMessage());
        }
    }



}
