ALTER TABLE `yuxi`.`picture`
    MODIFY COLUMN `is_delete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除 0存在 1删除' AFTER `edit_time`;