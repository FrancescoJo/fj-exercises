CREATE TABLE IF NOT EXISTS `apps` (
	`id` INT PRIMARY KEY AUTO_INCREMENT,
	`iconName` TEXT,
	`name` TEXT,
	`description` TEXT,
	`stageIds` TEXT,
	`platformIds` TEXT,
	`authToken` TEXT
);
