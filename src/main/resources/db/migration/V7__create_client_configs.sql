CREATE TABLE client_configs (
                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                client_id UUID NOT NULL REFERENCES clients(id),
                                config_key VARCHAR(100) NOT NULL,
                                config_value TEXT,
                                updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
                                UNIQUE(client_id, config_key)
);