CREATE TABLE customers
(
    customer_id      VARCHAR(255) PRIMARY KEY,
    email            VARCHAR(255)            NOT NULL,
    physical_address VARCHAR(255)            NOT NULL,
    created          TIMESTAMP DEFAULT NOW() NOT NULL,
    last_updated     TIMESTAMP DEFAULT NOW() NOT NULL
);