CREATE SCHEMA `internet_shop` DEFAULT CHARACTER SET utf8 ;
CREATE TABLE `internet_shop`.`items` (
  `item_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `price` DECIMAL(6,2) NOT NULL,
  PRIMARY KEY (`item_id`));
INSERT INTO items(name, price) VALUES('Item 1', 1.0);
INSERT INTO items(name, price) VALUES('Item 2', 2.0);
INSERT INTO items(name, price) VALUES('Item 3', 3.0);