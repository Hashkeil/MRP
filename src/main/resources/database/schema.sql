CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       token VARCHAR(255),
                       total_ratings INTEGER DEFAULT 0,
                       average_given_rating DECIMAL(3,2) DEFAULT 0.0,
                       favorite_genre VARCHAR(100),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE media_entries (
                               id SERIAL PRIMARY KEY,
                               title VARCHAR(255) NOT NULL,
                               description TEXT,
                               type VARCHAR(50) NOT NULL,
                               release_year INTEGER,
                               age_restriction VARCHAR(50),
                               creator_id BIGINT REFERENCES users(id),
                               average_rating DECIMAL(3,2) DEFAULT 0.0,
                               total_ratings INTEGER DEFAULT 0,
                               favorites_count INTEGER DEFAULT 0,
                               created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE genres (
                        id SERIAL PRIMARY KEY,
                        media_id BIGINT REFERENCES media_entries(id) ON DELETE CASCADE,
                        genre VARCHAR(100) NOT NULL
);

CREATE TABLE ratings (
                         id SERIAL PRIMARY KEY,
                         user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
                         media_id BIGINT REFERENCES media_entries(id) ON DELETE CASCADE,
                         stars INTEGER CHECK (stars >= 1 AND stars <= 5),
                         comment TEXT,
                         timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         confirmed BOOLEAN DEFAULT FALSE,
                         likes_count INTEGER DEFAULT 0,
                         UNIQUE(user_id, media_id)
);

CREATE TABLE favorites (
                           id SERIAL PRIMARY KEY,
                           user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
                           media_id BIGINT REFERENCES media_entries(id) ON DELETE CASCADE,
                           date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           UNIQUE(user_id, media_id)
);