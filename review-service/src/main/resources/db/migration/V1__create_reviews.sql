CREATE TABLE IF NOT EXISTS reviews (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    rating INT NOT NULL,
    title VARCHAR(255),
    content TEXT,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS votes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    review_id BIGINT NOT NULL,
    type VARCHAR(255),
    PRIMARY KEY (id),
    UNIQUE KEY uk_votes_user_review (user_id, review_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
