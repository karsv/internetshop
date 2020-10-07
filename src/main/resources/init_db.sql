DROP SCHEMA IF EXISTS `internet_shop`;
CREATE SCHEMA `internet_shop` DEFAULT CHARACTER SET utf8;
USE `internet_shop`;

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
                         `user_id` int(11) NOT NULL AUTO_INCREMENT,
                         `name` varchar(255) NOT NULL,
                         `password` varchar(255) NOT NULL,
                         `salt` varchar(255) NOT NULL,
                         `token` varchar(255) NOT NULL,
                         PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `bucket`;
CREATE TABLE `bucket` (
                          `bucket_id` int(11) NOT NULL AUTO_INCREMENT,
                          `user_id` int(11) NOT NULL,
                          PRIMARY KEY (`bucket_id`),
                          KEY `fk_bucket_userId_idx` (`user_id`),
                          CONSTRAINT `fk_bucket_userId` FOREIGN KEY (`user_id`)
                              REFERENCES `users` (`user_id`)
                              ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `items`;
CREATE TABLE `items` (
                         `item_id` int(11) NOT NULL AUTO_INCREMENT,
                         `name` varchar(255) NOT NULL,
                         `price` decimal(6,2) NOT NULL,
                         PRIMARY KEY (`item_id`),
                         UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `bucket_item`;
CREATE TABLE `bucket_item` (
                               `id` int(11) NOT NULL AUTO_INCREMENT,
                               `bucket_id` int(11) NOT NULL,
                               `item_id` int(11) NOT NULL,
                               PRIMARY KEY (`id`),
                               KEY `fk_bucketId_idx` (`bucket_id`),
                               KEY `fk_item_idx` (`item_id`),
                               CONSTRAINT `fk_bucketId` FOREIGN KEY (`bucket_id`)
                                   REFERENCES `bucket` (`bucket_id`)
                                   ON DELETE NO ACTION ON UPDATE NO ACTION,
                               CONSTRAINT `fk_item` FOREIGN KEY (`item_id`)
                                   REFERENCES `items` (`item_id`)
                                   ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `items` VALUES (1,'Item 1',1.00),
                           (2,'Item 2',2.00),(3,'Item 3',3.00),
                           (4,'Item 4',4.00),(5,'Item 5',5.00);

DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
                          `order_id` int(11) NOT NULL AUTO_INCREMENT,
                          `user_id` int(11) NOT NULL,
                          `amount` decimal(10,2) NOT NULL,
                          PRIMARY KEY (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `order_items`;
CREATE TABLE `order_items` (
                               `order_items_id` int(11) NOT NULL AUTO_INCREMENT,
                               `order_id` int(11) NOT NULL,
                               `item_id` int(11) NOT NULL,
                               PRIMARY KEY (`order_items_id`),
                               KEY `fk_order_idx` (`order_id`),
                               KEY `fk_items_idx` (`item_id`),
                               CONSTRAINT `fk_items` FOREIGN KEY (`item_id`)
                                   REFERENCES `items` (`item_id`)
                                   ON DELETE NO ACTION ON UPDATE NO ACTION,
                               CONSTRAINT `fk_order` FOREIGN KEY (`order_id`)
                                   REFERENCES `orders` (`order_id`)
                                   ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

INSERT INTO `orders` VALUES (3,15,7.00);
INSERT INTO `order_items` VALUES (1,3,1),(2,3,1),(3,3,1),(4,3,1),(5,3,1),(6,3,1),(7,3,1);

DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
                         `id` int(11) NOT NULL AUTO_INCREMENT,
                         `role_name` varchar(21) NOT NULL,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `roles_role_name_uindex` (`role_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

INSERT INTO `roles` VALUES (1,'ADMIN'),(2,'USER');

DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE `user_roles` (
                              `id` bigint(20) NOT NULL AUTO_INCREMENT,
                              `user_id` int(11) NOT NULL,
                              `role_id` int(11) NOT NULL,
                              PRIMARY KEY (`id`),
                              KEY `fk_user_id_idx` (`user_id`),
                              KEY `fk_roles_id_idx` (`role_id`),
                              CONSTRAINT `fk_roles_id` FOREIGN KEY (`role_id`)
                                  REFERENCES `roles` (`id`)
                                  ON DELETE NO ACTION ON UPDATE NO ACTION,
                              CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`)
                                  REFERENCES `users` (`user_id`)
                                  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

INSERT INTO `users`
VALUES (6,'Admin','10274-11086-87-29-1057738311453-124-6139738-110114-9959-15-41-738711212410615058-85-4975564-110-1-26-75-75-20-591-1871-115-11896-48-588861-101-88-1254033-120-385783735','1aGW0LS1chCGZDzWw8qYSA==',
        '12dd5d85-c0b6-458e-8153-a6ee8dd2f7a9');

INSERT INTO `user_roles` VALUES (2,6,1);