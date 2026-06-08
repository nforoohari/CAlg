CREATE
    DATABASE currency;

USE
    currency;

CREATE TABLE crypto_day
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    currency      INTEGER NOT NULL,
    interval_date datetime,
    open          DECIMAL(20, 8),
    high          DECIMAL(20, 8),
    low           DECIMAL(20, 8),
    close         DECIMAL(20, 8),
    volume        DECIMAL(20, 8),
    INDEX idx_time (interval_date)
);

CREATE TABLE crypto_hour
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    currency      INTEGER NOT NULL,
    interval_date datetime,
    open          DECIMAL(20, 8),
    high          DECIMAL(20, 8),
    low           DECIMAL(20, 8),
    close         DECIMAL(20, 8),
    volume        DECIMAL(20, 8),
    INDEX idx_time (interval_date)
);

CREATE TABLE crypto_minute
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    currency      INTEGER NOT NULL,
    interval_date datetime,
    open          DECIMAL(20, 8),
    high          DECIMAL(20, 8),
    low           DECIMAL(20, 8),
    close         DECIMAL(20, 8),
    volume        DECIMAL(20, 8),
    INDEX idx_time (interval_date)
);

CREATE TABLE crypto_second
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    currency      INTEGER NOT NULL,
    interval_date datetime,
    open          DECIMAL(20, 8),
    high          DECIMAL(20, 8),
    low           DECIMAL(20, 8),
    close         DECIMAL(20, 8),
    volume        DECIMAL(20, 8),
    INDEX idx_time (interval_date)
);

CREATE TABLE order_request
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    exchange     INTEGER        NOT NULL,
    currency     INTEGER        NOT NULL,
    side         INTEGER        NOT NULL,
    capital      DECIMAL(20, 8) NOT NULL,
    price        DECIMAL(20, 8) NOT NULL,
    fee          DECIMAL(20, 8) NOT NULL,
    request_date datetime,
    INDEX idx_time (request_date)
);

CREATE TABLE order_state
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    request_id  BIGINT         NOT NULL,
    order_id    BIGINT         NOT NULL,
    order_date  datetime,
    volume      DECIMAL(20, 8) NOT NULL,
    balance     DECIMAL(20, 8) NOT NULL,
    payedFee    DECIMAL(20, 8) NOT NULL,
    status      INTEGER        NOT NULL,
    status_date datetime,
    INDEX idx_time (order_date),
    CONSTRAINT fk_state FOREIGN KEY (request_id) REFERENCES order_request (id) ON DELETE CASCADE
);

CREATE TABLE order_transaction
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    request_id       BIGINT         NOT NULL,
    order_id         BIGINT         NOT NULL,
    side             INTEGER        NOT NULL,
    volume           DECIMAL(20, 8) NOT NULL,
    price            DECIMAL(20, 8) NOT NULL,
    fee              DECIMAL(20, 8) NOT NULL,
    payedFee         DECIMAL(20, 8) NOT NULL,
    balance          DECIMAL(20, 8) NOT NULL,
    transaction_date datetime,
    INDEX idx_time (transaction_date),
    CONSTRAINT fk_transaction FOREIGN KEY (request_id) REFERENCES order_request (id) ON DELETE CASCADE
);

CREATE TABLE trader
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    exchange    INTEGER        NOT NULL,
    currency    INTEGER        NOT NULL,
    fee         DECIMAL(20, 8) NOT NULL,
    interval    INTEGER        NOT NULL,
    trader_date datetime,
    start_time  VARCHAR(100),
    end_time    VARCHAR(100),
    INDEX idx_time (trader_date)
);

CREATE TABLE trader_state
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    trader_id  BIGINT         NOT NULL,
    state_date datetime,
    volume     DECIMAL(20, 8) NOT NULL,
    balance    DECIMAL(20, 8) NOT NULL,
    payedFee   DECIMAL(20, 8) NOT NULL,
    INDEX idx_time (state_date),
    CONSTRAINT fk_trader_state FOREIGN KEY (trader_id) REFERENCES trader (id) ON DELETE CASCADE
);

CREATE TABLE trader_settings
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    trader_id         BIGINT         NOT NULL,
    settings_date     datetime,
    threshold_price   DECIMAL(20, 8) NOT NULL,
    stop_loss_percent DECIMAL(20, 8) NOT NULL,
    stop_loss         DECIMAL(20, 8) NOT NULL,
    delta_percent     DECIMAL(20, 8) NOT NULL,
    delta             DECIMAL(20, 8) NOT NULL,
    ascending_percent DECIMAL(20, 8) NOT NULL,
    ascending         DECIMAL(20, 8) NOT NULL,
    top_fix           BOOLEAN,
    bottom_fix        BOOLEAN,
    INDEX idx_time (settings_date),
    CONSTRAINT fk_trader_settings FOREIGN KEY (trader_id) REFERENCES trader (id) ON DELETE CASCADE
);