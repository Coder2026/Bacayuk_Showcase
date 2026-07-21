DROP TABLE IF EXISTS posts;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    photo_key TEXT,
    location GEOGRAPHY(Point, 4326),
    email_verified BOOLEAN
);

CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE posts (
    id VARCHAR(255) PRIMARY KEY,
    owner_id VARCHAR(255) NOT NULL,
    title VARCHAR(255),
    description TEXT,
    category_id BIGINT NOT NULL,
    guarantee DOUBLE PRECISION,
    photo_key VARCHAR(255),

    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories(id),
    CONSTRAINT fk_owner FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE INDEX idx_posts_location ON posts USING GIST (location);


CREATE INDEX idx_posts_category_location ON posts (category_id, location);