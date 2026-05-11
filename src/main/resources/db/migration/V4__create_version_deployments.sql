CREATE TABLE version_deployments (
                                     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                     version_id BIGINT NOT NULL REFERENCES versions(id),
                                     deployment_type VARCHAR(10) NOT NULL,
                                     status VARCHAR(15) NOT NULL DEFAULT 'PENDING',
                                     notes TEXT,
                                     created_by VARCHAR(100) NOT NULL,
                                     created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE deployment_clients (
                                    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                    deployment_id UUID NOT NULL REFERENCES version_deployments(id),
                                    client_id UUID NOT NULL REFERENCES clients(id),
                                    status VARCHAR(15) NOT NULL DEFAULT 'PENDING',
                                    started_at TIMESTAMP,
                                    finished_at TIMESTAMP
);