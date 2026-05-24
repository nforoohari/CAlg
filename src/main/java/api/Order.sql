CREATE DATABASE crypto;

USE crypto;

CREATE TABLE order_status(
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             crypto BIGINT NOT NULL,
                             side ENUM('BUY', 'SELL') NOT NULL,
                             volume DECIMAL(20, 8) NOT NULL,
                             price DECIMAL(20, 8) NOT NULL,
                             ordered_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                             completed BOOLEAN DEFAULT FALSE,
                             completed_date DATETIME NULL,
                             INDEX idx_time(ordered_date)
);

CREATE TABLE order_details(
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              order_status_id BIGINT NOT NULL,
                              volume DECIMAL(20, 8) NOT NULL,
                              price DECIMAL(20, 8) NOT NULL,
                              detail_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                              INDEX idx_time(detail_date),
                              CONSTRAINT fk_order FOREIGN KEY(order_status_id) REFERENCES order_status(id) ON DELETE CASCADE
);
