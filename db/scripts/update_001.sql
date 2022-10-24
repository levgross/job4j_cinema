CREATE TABLE IF NOT EXISTS users (
  id SERIAL PRIMARY KEY,
  username VARCHAR NOT NULL,
  email VARCHAR NOT NULL UNIQUE,
  phone VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS sessions (
  id SERIAL PRIMARY KEY,
  name VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS ticket (
    id SERIAL PRIMARY KEY,
    session_id INT NOT NULL REFERENCES sessions(id),
    pos_row INT NOT NULL,
    cell INT NOT NULL,
    user_id INT NOT NULL REFERENCES users(id),
    CONSTRAINT ticket_unique UNIQUE (session_id, pos_row, cell)
);