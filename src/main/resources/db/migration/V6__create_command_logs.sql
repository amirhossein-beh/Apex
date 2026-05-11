CREATE TABLE command_logs (
                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              client_id UUID NOT NULL REFERENCES clients(id),
                              action VARCHAR(50) NOT NULL,
                              payload JSONB,
                              status VARCHAR(10) NOT NULL DEFAULT 'PENDING',
                              sent_at TIMESTAMP NOT NULL DEFAULT NOW(),
                              result_at TIMESTAMP
);