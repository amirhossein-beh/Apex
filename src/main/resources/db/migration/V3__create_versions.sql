CREATE TABLE versions (
                          id BIGSERIAL PRIMARY KEY,
                          version_number VARCHAR(20) NOT NULL UNIQUE,
                          file_path VARCHAR(500) NOT NULL,
                          checksum VARCHAR(64) NOT NULL,
                          is_active BOOLEAN NOT NULL DEFAULT FALSE,
                          is_mandatory BOOLEAN NOT NULL DEFAULT FALSE,
                          min_version VARCHAR(20),
                          release_notes TEXT,
                          created_by VARCHAR(100) NOT NULL,
                          created_at TIMESTAMP NOT NULL DEFAULT NOW()
);