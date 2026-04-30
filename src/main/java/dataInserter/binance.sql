CREATE DATABASE crypto;

USE crypto;

CREATE TABLE eth_usdt_1s (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             open_time BIGINT,
                             open_price DOUBLE,
                             high_price DOUBLE,
                             low_price DOUBLE,
                             close_price DOUBLE,
                             volume DOUBLE,
                             INDEX idx_time (open_time)
);
