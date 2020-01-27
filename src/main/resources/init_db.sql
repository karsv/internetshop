CREATE SCHEMA `internet_shop` DEFAULT CHARACTER SET utf8;

CREATE TABLE `internet_shop`.`items`
(
    `item_id` INT           NOT NULL AUTO_INCREMENT,
    `name`    VARCHAR(255)  NOT NULL,
    `price`   DECIMAL(6, 2) NOT NULL,
    PRIMARY KEY (`item_id`)
);

INSERT INTO items(name, price)
VALUES ('Item 1', 1.0);
INSERT INTO items(name, price)
VALUES ('Item 2', 2.0);
INSERT INTO items(name, price)
VALUES ('Item 3', 3.0);

create table roles
(
    id        int auto_increment,
    role_name varchar(21) not null,
    UNIQUE (role_name),
    constraint roles_pk
        primary key (id)
);

create table users
(
    id       int auto_increment,
    name     varchar(255) not null,
    password varchar(255) not null,
    token    varchar(255) not null,
    constraint users_pk
        primary key (id)
);

CREATE TABLE `internet_shop`.`user_roles`
(
    `id`       INT NOT NULL AUTO_INCREMENT,
    `user_id`  INT NOT NULL,
    `roles_id` INT NOT NULL,
    PRIMARY KEY (`id`)
);

ALTER TABLE `internet_shop`.`user_roles`
    ADD INDEX `fk_user_id_idx` (`user_id` ASC),
    ADD INDEX `fk_roles_id_idx` (`roles_id` ASC);
;
ALTER TABLE `internet_shop`.`user_roles`
    ADD CONSTRAINT `fk_user_id`
        FOREIGN KEY (`user_id`)
            REFERENCES `internet_shop`.`users` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    ADD CONSTRAINT `fk_roles_id`
        FOREIGN KEY (`role_id`)
            REFERENCES `internet_shop`.`roles` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;

ALTER TABLE `internet_shop`.`items`
    CHANGE COLUMN `item_id` `item_id` INT(11) NOT NULL,
    ADD UNIQUE INDEX `name_UNIQUE` (`name` ASC);

CREATE TABLE `internet_shop`.`bucket`
(
    `bucket_id` INT NOT NULL AUTO_INCREMENT,
    `user_id`   INT NOT NULL,
    PRIMARY KEY (`bucket_id`),
    INDEX `fk_bucket_userId_idx` (`user_id` ASC),
    CONSTRAINT `fk_bucket_userId`
        FOREIGN KEY (`user_id`)
            REFERENCES `internet_shop`.`users` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

CREATE TABLE `internet_shop`.`bucket_item`
(
    `id`        INT NOT NULL,
    `bucket_id` INT NOT NULL,
    `item_id`   INT NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_bucketId_idx` (`bucket_id` ASC),
    INDEX `fk_itemId_idx` (`item_id` ASC),
    CONSTRAINT `fk_bucketId`
        FOREIGN KEY (`bucket_id`)
            REFERENCES `internet_shop`.`bucket` (`bucket_id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_itemId`
        FOREIGN KEY (`item_id`)
            REFERENCES `internet_shop`.`items` (`item_id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

CREATE TABLE `internet_shop`.`orders`
(
    `order_id` INT            NOT NULL AUTO_INCREMENT,
    `user_id`  INT            NOT NULL,
    `amount`   DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (`order_id`)
);

CREATE TABLE `internet_shop`.`order_items`
(
    `order_items_id` INT NOT NULL AUTO_INCREMENT,
    `order_id`       INT NOT NULL,
    `item_id`        INT NOT NULL,
    PRIMARY KEY (`order_items_id`),
    INDEX `fk_order_idx` (`order_id` ASC),
    INDEX `fk_items_idx` (`item_id` ASC),
    CONSTRAINT `fk_order`
        FOREIGN KEY (`order_id`)
            REFERENCES `internet_shop`.`orders` (`order_id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_items`
        FOREIGN KEY (`item_id`)
            REFERENCES `internet_shop`.`items` (`item_id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);
