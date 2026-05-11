Create TABLE Clients(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    location VARCHAR(200),
    client_version VARCHAR(20),
    status VARCHAR(10) NOT NULL DEFAULT 'OFFLINE',
    last_seen TIMESTAMP,
    token VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
)
