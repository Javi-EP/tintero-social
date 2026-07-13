CREATE TABLE IF NOT EXISTS reading_lists (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    is_private BIT DEFAULT FALSE,
    created_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS reading_list_items (
    id BIGINT NOT NULL AUTO_INCREMENT,
    book_id BIGINT NOT NULL,
    progress INT DEFAULT 0,
    added_at DATETIME(6),
    finished_at DATETIME(6),
    reading_list_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_rli_reading_list FOREIGN KEY (reading_list_id) REFERENCES reading_lists (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
