package cl.duoc.reviews.service;

import org.springframework.stereotype.Service;

import cl.duoc.reviews.client.DestinationClient;
import cl.duoc.reviews.client.LoginClient;
import cl.duoc.reviews.dto.ApiResponse;
import cl.duoc.reviews.dto.CreateRequestDTO;
import cl.duoc.reviews.dto.ReviewResponseDTO;
import cl.duoc.reviews.dto.destination.DestinationResponseDTO;
import cl.duoc.reviews.dto.user.UserDTO;
import cl.duoc.reviews.exception.custom.ResourceNotFoundException;
import cl.duoc.reviews.exception.custom.UnauthorizedException;
import cl.duoc.reviews.model.Review;
import cl.duoc.reviews.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final LoginClient loginClient;
    private final DestinationClient destinationClient;

    // crear review 
    public ApiResponse<ReviewResponseDTO> createReview(CreateRequestDTO dto, String token) {
    // 1. Validar token y obtener usuario real (Login Service)
    UserDTO user = loginClient.validateToken(token);
    if (user == null) {
        throw new UnauthorizedException("Token inválido");
    }

    // 2. Validar destino y traer name/country (Destination Service)
    DestinationResponseDTO destination = destinationClient.getDestinationById(dto.getDestinationId(), token);
    if (destination == null) {
        throw new ResourceNotFoundException("El destino no existe");
    }

    // 3. Guardar review (usa user.getUsername(), no dto.getUsername() directamente,
    //    así evitas que alguien falsifique el username en el body)
    Review review = new Review();
    review.setUsername(user.getUsername());
    review.setDestinationId(dto.getDestinationId());
    review.setRating(dto.getRating());
    review.setComment(dto.getComment());
    reviewRepository.save(review);

    // 4. Armar respuesta
    ReviewResponseDTO response = new ReviewResponseDTO(
        review.getId(), review.getUsername(), review.getDestinationId(),
        destination.getName(), destination.getCountry(),
        review.getRating(), review.getComment(), review.getCreatedAt()
    );

    return new ApiResponse<>(201, "Operación exitosa", response);
}

// buscar review -> id
public ApiResponse<ReviewResponseDTO> getReviewById(Long id, String token) {
    Review review = reviewRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reseña no encontrada"));

    DestinationResponseDTO destination = destinationClient.getDestinationById(review.getDestinationId(), token);
    if (destination == null) {
        throw new ResourceNotFoundException("El destino asociado ya no existe");
    }

    ReviewResponseDTO response = new ReviewResponseDTO(
            review.getId(), review.getUsername(), review.getDestinationId(),
            destination.getName(), destination.getCountry(),
            review.getRating(), review.getComment(), review.getCreatedAt()
    );

    return new ApiResponse<>(200, "Operación exitosa", response);
}

// eliminar review -> id
public ApiResponse<Void> deleteReview(Long id, String token) {
    UserDTO user = loginClient.validateToken(token);
    if (user == null) {
        throw new UnauthorizedException("Token inválido");
    }

    Review review = reviewRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reseña no encontrada"));

    // Solo el autor puede eliminar su propia reseña
    if (!review.getUsername().equals(user.getUsername())) {
        throw new UnauthorizedException("No tienes permiso para eliminar esta reseña");
    }

    reviewRepository.delete(review);
    return new ApiResponse<>(204, "Operación exitosa", null);

}

// buscar todas las reviews
public ApiResponse<List<ReviewResponseDTO>> getAllReviews(String token) {
    List<Review> reviews = reviewRepository.findAll();

    List<ReviewResponseDTO> response = reviews.stream()
            .map(review -> {
                DestinationResponseDTO destination = destinationClient.getDestinationById(review.getDestinationId(), token);
                String name = destination != null ? destination.getName() : "Desconocido";
                String country = destination != null ? destination.getCountry() : "Desconocido";

                return new ReviewResponseDTO(
                        review.getId(), review.getUsername(), review.getDestinationId(),
                        name, country,
                        review.getRating(), review.getComment(), review.getCreatedAt()
                );
            })
            .collect(Collectors.toList());

    return new ApiResponse<>(200, "Operación exitosa", response);
}
}
