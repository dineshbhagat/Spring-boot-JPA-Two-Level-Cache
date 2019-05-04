CREATE TABLE `customer`
(
    `id`            int(8) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'incremental id of customer',
    `name`          VARCHAR(45)     NULL COMMENT 'Name of customer',
    `address`       VARCHAR(45)     NULL COMMENT 'address of customer',
    `mobile_number` VARCHAR(45)     NOT NULL COMMENT 'Customer’s mobile number',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `mobile_number_UNIQUE` (`mobile_number` ASC) VISIBLE
);

CREATE TABLE `broadband`
(
    `id`          int(8) unsigned NOT NULL AUTO_INCREMENT,
    `plan`        varchar(45)          DEFAULT NULL,
    `is_active`   TINYINT(1)      NULL DEFAULT 0,
    `customer_id` int(10) unsigned     DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `customer_id_fk_idx` (`customer_id`),
    CONSTRAINT `customer_id_fk` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
);

CREATE TABLE `dth_subs`
(
    `id`          int(8) unsigned NOT NULL AUTO_INCREMENT,
    `plan`        varchar(45)      DEFAULT NULL,
    `customer_id` int(10) unsigned DEFAULT NULL,
    `is_active`   tinyint(1)       DEFAULT '0',
    PRIMARY KEY (`id`)
);

INSERT INTO `test`.`customer`
(`name`,
 `address`,
 `mobile_number`)
VALUES ("A",
        "Address",
        "111111");

INSERT INTO `test`.`customer` (`name`, `address`, `mobile_number`) VALUES ('B', 'Add', '222222222');

INSERT INTO `test`.`dth_subs` (`plan`, `customer_id`, `is_active`) VALUES ('UNLIMITED', '1', '1');

INSERT INTO `test`.`broadband` (`plan`, `is_active`, `customer_id`) VALUES ('UNLIMITED', '1', '1');

