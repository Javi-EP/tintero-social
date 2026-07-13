CREATE TABLE IF NOT EXISTS recommendations (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    score DOUBLE,
    reason VARCHAR(255),
    dismissed BIT DEFAULT FALSE,
    created_at DATETIME(6),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS genre_preferences (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    genre_id BIGINT NOT NULL,
    weight DOUBLE,
    PRIMARY KEY (id),
    UNIQUE KEY uk_genre_pref_user_genre (user_id, genre_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
