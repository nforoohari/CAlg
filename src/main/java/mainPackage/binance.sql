CREATE DATABASE crypto;

USE crypto;

CREATE TABLE crypto_day (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             crypto BIGINT,
                             date BIGINT,
                             open DOUBLE,
                             high DOUBLE,
                             low DOUBLE,
                             close DOUBLE,
                             volume DOUBLE,
                             INDEX idx_time (date)
);
