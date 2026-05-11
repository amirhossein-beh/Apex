CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(20) NOT NULL DEFAULT 'VIEWER',
                       is_active BOOLEAN NOT NULL DEFAULT TRUE,
                       is_deletable BOOLEAN NOT NULL DEFAULT TRUE,
                       created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

INSERT INTO users (username, password, role, is_deletable)
VALUES (
           'amirhossein',
           '$2a$12$placeholder',
           'ADMIN',
           FALSE
       );