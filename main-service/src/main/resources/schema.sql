CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    email VARCHAR(254) UNIQUE,
    name VARCHAR(250),
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    name VARCHAR(50) UNIQUE,
    CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events (
    id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    title VARCHAR(120),
    annotation VARCHAR(2000),
    category_id BIGINT REFERENCES categories (id),
    description VARCHAR(7000),
    event_date TIMESTAMP WITHOUT TIME ZONE,
    lat FLOAT,
    lon FLOAT,
    paid BOOLEAN,
    participant_limit INTEGER,
    request_moderation BOOLEAN,
    confirmed_requests INTEGER,
    created_on TIMESTAMP WITHOUT TIME ZONE,
    initiator_id BIGINT REFERENCES users (id),
    published_on TIMESTAMP WITHOUT TIME ZONE,
    state VARCHAR,
    views INTEGER,
    CONSTRAINT pk_event PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS requests (
    id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE,
    event_id BIGINT REFERENCES events (id) ON DELETE CASCADE,
    requester_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    status varchar,
    CONSTRAINT pk_request PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilations (
    id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    pinned BOOLEAN,
    title varchar(50),
    CONSTRAINT pk_compilation PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilations_events (
    events_id BIGINT NOT NULL REFERENCES events (id) ON DELETE SET NULL,
    compilation_id BIGINT REFERENCES compilations (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS events_likes (
    event_id BIGINT NOT NULL REFERENCES events (id) ON DELETE SET NULL,
    estimater_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    positive INTEGER,
    CONSTRAINT pk_likes PRIMARY KEY (event_id, estimater_id),
    CONSTRAINT positive_values CHECK (positive = 1 OR positive = -1)
);