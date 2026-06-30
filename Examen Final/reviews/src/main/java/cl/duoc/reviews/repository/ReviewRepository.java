package cl.duoc.reviews.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.reviews.model.Review;

public interface ReviewRepository  extends JpaRepository<Review, Long>{

}
