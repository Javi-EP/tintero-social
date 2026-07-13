CREATE TABLE IF NOT EXISTS stat_snapshots (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    books_read INT NOT NULL DEFAULT 0,
    books_reading INT NOT NULL DEFAULT 0,
    books_want_to_read INT NOT NULL DEFAULT 0,
    total_reviews INT NOT NULL DEFAULT 0,
    average_rating DOUBLE,
    favorite_genre VARCHAR(100) DEFAULT 'N/A',
    last_updated DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_stat_snapshots_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
