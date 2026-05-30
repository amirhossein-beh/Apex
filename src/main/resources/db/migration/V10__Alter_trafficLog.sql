-- ALTER TABLE traffic_logs
--     RENAME COLUMN ref_Id TO ref_Id;

ALTER TABLE traffic_logs ALTER COLUMN log_date TYPE VARCHAR(20);
ALTER TABLE traffic_logs ALTER COLUMN log_time TYPE VARCHAR(20);