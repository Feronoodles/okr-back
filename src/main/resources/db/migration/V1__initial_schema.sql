CREATE TABLE IF NOT EXISTS `user` (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at DATE NULL
);

CREATE INDEX IF NOT EXISTS idx_user_username ON `user` (username);

CREATE TABLE IF NOT EXISTS key_results (
    kr_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    percent DOUBLE NOT NULL,
    quarter VARCHAR(255) NOT NULL,
    owner VARCHAR(255) NOT NULL,
    iniciativa VARCHAR(255) NOT NULL,
    pilar VARCHAR(255) NOT NULL,
    area VARCHAR(255) NOT NULL,
    objective VARCHAR(500) NOT NULL,
    objective_percent DOUBLE NOT NULL,
    archived BOOLEAN NOT NULL DEFAULT FALSE,
    archived_at TIMESTAMP NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_key_results_user FOREIGN KEY (user_id) REFERENCES `user` (user_id)
);

CREATE INDEX IF NOT EXISTS idx_key_results_user_id ON key_results (user_id);
CREATE INDEX IF NOT EXISTS idx_key_results_archived ON key_results (archived);

CREATE TABLE IF NOT EXISTS refresh_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token_key VARCHAR(36) NOT NULL,
    token_hash VARCHAR(100) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    revoked_at TIMESTAMP NULL,
    last_used_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT uk_refresh_tokens_token_key UNIQUE (token_key),
    CONSTRAINT fk_refresh_tokens_user FOREIGN KEY (user_id) REFERENCES `user` (user_id)
);

CREATE INDEX IF NOT EXISTS idx_refresh_tokens_user_id ON refresh_tokens (user_id);
CREATE INDEX IF NOT EXISTS idx_refresh_tokens_revoked_at ON refresh_tokens (revoked_at);
