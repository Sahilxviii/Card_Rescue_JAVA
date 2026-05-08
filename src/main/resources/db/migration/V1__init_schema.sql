CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255)
);

CREATE TABLE lost_cards (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    card_type VARCHAR(255) NOT NULL,
    bank_name VARCHAR(255) NOT NULL,
    last4_digits VARCHAR(4) NOT NULL,
    status VARCHAR(255) DEFAULT 'OPEN'
);

CREATE TABLE found_cards (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    card_type VARCHAR(10) NOT NULL,
    bank_name VARCHAR(100) NOT NULL,
    last4_digits VARCHAR(4) NOT NULL,
    status VARCHAR(255) DEFAULT 'OPEN'
);

CREATE TABLE matches (
    id BIGSERIAL PRIMARY KEY,
    lost_card_id BIGINT REFERENCES lost_cards(id) ON DELETE CASCADE,
    found_card_id BIGINT REFERENCES found_cards(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    matched_at TIMESTAMP
);