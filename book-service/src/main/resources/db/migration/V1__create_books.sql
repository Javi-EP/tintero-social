CREATE TABLE IF NOT EXISTS books (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(255),
    genre VARCHAR(255),
    synopsis VARCHAR(2000),
    publication_year INT,
    PRIMARY KEY (id),
    UNIQUE KEY uk_books_isbn (isbn)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
