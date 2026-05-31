CREATE DATABASE currency;

USE currency;

CREATE TABLE crypto_day (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             currency BIGINT,
                             interval_date datetime,
                             open DOUBLE,
                             high DOUBLE,
                             low DOUBLE,
                             close DOUBLE,
                             volume DOUBLE,
                             INDEX idx_time (interval_date)
);
