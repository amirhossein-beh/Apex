CREATE TABLE client_version_history (
                                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                        client_id UUID NOT NULL REFERENCES clients(id),
                                        version_number VARCHAR(20) NOT NULL,
                                        previous_version VARCHAR(20),
                                        status VARCHAR(15) NOT NULL,
                                        started_at TIMESTAMP NOT NULL DEFAULT NOW(),
                                        finished_at TIMESTAMP
);