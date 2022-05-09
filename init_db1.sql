CREATE TABLE scheduled_kafka_messages
(
    customer_id      VARCHAR(255) PRIMARY KEY,
    email            VARCHAR(255)            NOT NULL,
    physical_address VARCHAR(255)            NOT NULL,
    processed        BOOLEAN   DEFAULT FALSE NOT NULL,
    created          TIMESTAMP DEFAULT NOW() NOT NULL,
    last_updated     TIMESTAMP DEFAULT NOW() NOT NULL
);