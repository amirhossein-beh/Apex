CREATE TABLE traffic_logs (
                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              client_id UUID NOT NULL REFERENCES clients(id),
                              plate_text VARCHAR(50),
                              confidence DOUBLE PRECISION,
                              country VARCHAR(50),
                              direction VARCHAR(5),
                              stream_id INTEGER,
                              plate_image_path VARCHAR(500),
                              car_image_path VARCHAR(500),
                              log_date VARCHAR(10),
                              log_time VARCHAR(10),
                              received_at TIMESTAMP NOT NULL DEFAULT NOW(),
                              forwarded_to_ghadir BOOLEAN NOT NULL DEFAULT FALSE,
                              forwarded_at TIMESTAMP
);