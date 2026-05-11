CREATE TABLE install_keys (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    key VARCHAR(50) NOT NULL UNIQUE,
    client_id UUID REFERENCES clients(id),
    is_used BOOLEAN NOT NULL DEFAULT FALSE,
    created_by VARCHAR(100) NOT NULL,
    used_at TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);