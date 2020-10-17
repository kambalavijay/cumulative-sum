CREATE TABLE `sys`.`num_time_table` (
  `num` INT NOT NULL,
  `_timestamp` DATETIME NOT NULL,
  INDEX `index1` (`_timestamp` ASC) VISIBLE);