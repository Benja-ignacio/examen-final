CREATE TABLE review (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    destination_id CHAR(36) NOT NULL,
    rating INT NOT NULL,
    comment VARCHAR(500) NOT NULL,
    created_at DATETIME NOT NULL,
    CONSTRAINT chk_rating CHECK (rating >= 0 AND rating <= 5)
);