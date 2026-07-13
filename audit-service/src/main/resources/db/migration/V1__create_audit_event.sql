CREATE TABLE IF NOT EXISTS audit_event (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT,
    event_type VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    resource_id BIGINT,
    resource_type VARCHAR(255),
    created_at DATETIME(6),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
