CREATE TABLE IF NOT EXISTS `uploadHistory` (
	`id` INT PRIMARY KEY AUTO_INCREMENT,
	`appId` INT NOT NULL,
	`stageId` INT NOT NULL,
	`versionName` TEXT NOT NULL,
	`binaryName` TEXT NOT NULL,
	`description` TEXT,
	`tags` TEXT,
	`uploadTime` INT,

	INDEX uploadHistory_appId (appId),
	FOREIGN KEY (appId)
		REFERENCES apps(id)
		ON DELETE CASCADE,
	INDEX uploadHistory_stageId (stageId),
	FOREIGN KEY (stageId)
		REFERENCES distStages(id)
);

ALTER TABLE `uploadHistory`
MODIFY COLUMN `uploadTime` BIGINT