DROP TABLE IF EXISTS compilations_events;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS participation;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS locations;
DROP TABLE IF EXISTS compilations;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email VARCHAR(256) NOT NULL UNIQUE,
    name VARCHAR(256) NOT NULL
);

CREATE TABLE IF NOT EXISTS categories (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) UNIQUE
);

CREATE TABLE IF NOT EXISTS locations (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    lat NUMERIC,
    lon NUMERIC
);


CREATE TABLE IF NOT EXISTS compilations (
    id iNTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    pinned BOOLEAN,
    title VARCHAR(50) UNIQUE
);

CREATE TABLE IF NOT EXISTS events (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    annotation VARCHAR(2000) NOT NULL,
    description VARCHAR(7000) NOT NULL,
    category_id INTEGER REFERENCES categories (id) NOT NULL,
    confirmed_requests INTEGER,
    created_on TIMESTAMP NOT NULL,
    initiator_id INTEGER REFERENCES users (id) NOT NULL,
    location_id INTEGER REFERENCES locations (id) NOT NULL,
    paid BOOLEAN NOT NULL,
    participant_limit INTEGER,
    published_on TIMESTAMP,
    event_date TIMESTAMP NOT NULL,
    request_moderation BOOLEAN NOT NULL,
    event_state VARCHAR(256) NOT NULL,
    title VARCHAR(120) NOT NULL
);

CREATE TABLE IF NOT EXISTS participation (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    event_id INTEGER REFERENCES events (id),
    created TIMESTAMP,
    requester_id INTEGER REFERENCES users (id),
    status VARCHAR(256),
    CONSTRAINT unique_request UNIQUE (event_id, requester_id)
);

CREATE TABLE IF NOT EXISTS compilations_events (
    compilation_id INTEGER REFERENCES compilations (id),
    event_id INTEGER REFERENCES events (id),
    CONSTRAINT pk_compilation_of_events PRIMARY KEY (compilation_id, event_id)
);

CREATE TABLE IF NOT EXISTS comments (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    event_id INTEGER REFERENCES events (id),
    creator_id INTEGER REFERENCES users (id),
    text VARCHAR(2000) NOT NULL,
    created TIMESTAMP NOT NULL
)
