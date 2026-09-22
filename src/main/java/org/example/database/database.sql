CREATE DATABASE IF NOT EXISTS iwish_db;

USE iwish_db;

CREATE TABLE IF NOT EXISTS clients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    name VARCHAR(150) NOT NULL,
    date_of_birth DATE
);

CREATE TABLE IF NOT EXISTS gift_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    price DOUBLE NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS wish_lists (
    id INT AUTO_INCREMENT PRIMARY KEY,
    client_id INT NOT NULL,
    name VARCHAR(150) NOT NULL,
    CONSTRAINT fk_wishlist_client
        FOREIGN KEY (client_id)
        REFERENCES clients(id)
        ON DELETE CASCADE,
    UNIQUE (client_id, name)
);

CREATE TABLE IF NOT EXISTS wishlist_items (
    wishlist_id INT NOT NULL,
    gift_item_id INT NOT NULL,
    PRIMARY KEY (wishlist_id, gift_item_id),
    CONSTRAINT fk_wishlist_items_wishlist
        FOREIGN KEY (wishlist_id)
        REFERENCES wish_lists(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_wishlist_items_gift
        FOREIGN KEY (gift_item_id)
        REFERENCES gift_items(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS friend_requests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    date_created DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    CONSTRAINT fk_friend_request_sender
        FOREIGN KEY (sender_id)
        REFERENCES clients(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_friend_request_receiver
        FOREIGN KEY (receiver_id)
        REFERENCES clients(id)
        ON DELETE CASCADE,
    CONSTRAINT chk_friend_request_status
        CHECK (status IN ('PENDING', 'ACCEPTED', 'DECLINED')),
    UNIQUE (sender_id, receiver_id)
);

CREATE TABLE IF NOT EXISTS contributions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    client_id INT NOT NULL,
    gift_item_id INT NOT NULL,
    wishlist_id INT NOT NULL,
    amount DOUBLE NOT NULL DEFAULT 0,
    CONSTRAINT fk_contribution_client
        FOREIGN KEY (client_id)
        REFERENCES clients(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_contribution_gift
        FOREIGN KEY (gift_item_id)
        REFERENCES gift_items(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_contribution_wishlist
        FOREIGN KEY (wishlist_id)
        REFERENCES wish_lists(id)
        ON DELETE CASCADE,
    CHECK (amount >= 0)
);

CREATE TABLE IF NOT EXISTS notifications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    client_id INT NOT NULL,
    type VARCHAR(50) NOT NULL,
    message VARCHAR(500) NOT NULL,
    notification_date DATETIME NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_notification_client
        FOREIGN KEY (client_id)
        REFERENCES clients(id)
        ON DELETE CASCADE
);
