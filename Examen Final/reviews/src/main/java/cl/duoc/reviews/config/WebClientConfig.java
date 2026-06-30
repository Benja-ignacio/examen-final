package cl.duoc.reviews.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    
    @Bean(name = "destinationWebClient")
    public WebClient webClient(WebClient.Builder builder) {
        return builder
        .baseUrl("http://DESTINATION/api/v1/destination/destinations").build();
    }

    
    @Bean(name = "loginWebClient")
    public WebClient loginWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://LOGIN/api/v1/users") // ajusta el path real de Login Service
                .build();
    }

}
