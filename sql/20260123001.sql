ALTER TABLE `yuxi`.`picture_space`
    MODIFY COLUMN `space_max_size` bigint NULL DEFAULT NULL COMMENT '空间的大小' AFTER `space_level`,
    MODIFY COLUMN `current_size` bigint NULL DEFAULT NULL COMMENT '当前空间下图片的总大小' AFTER `space_max_number`;