package cl.duoc.reviews.client;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.core.ParameterizedTypeReference;

import cl.duoc.reviews.dto.ApiResponse;
import java.time.Duration;
import cl.duoc.reviews.dto.user.UserDTO;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class LoginClient {
    @Qualifier("loginWebClient")
    private final WebClient webClient;


        public UserDTO validateToken(String token) {
        try {
            ApiResponse<UserDTO> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/validate")
                            .queryParam("token", token)
                            .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ApiResponse<UserDTO>>() {})
                    .timeout(Duration.ofSeconds(3))
                    .block();

            if (response == null || response.getData() == null) {
                return null;
            }
            return response.getData();

        } catch (WebClientResponseException.Unauthorized ex) {
            return null; // token inválido
        } catch (Exception ex) {
            throw new RuntimeException("Error al comunicarse con Login Service: " + ex.getMessage());
        }
    }

}
