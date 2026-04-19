ALTER TABLE `yuxi`.`picture_space`
    MODIFY COLUMN `space_max_size` bigint NOT NULL COMMENT '空间的大小' AFTER `space_level`,
    MODIFY COLUMN `space_max_number` bigint NOT NULL COMMENT '空间的图片容量' AFTER `space_max_size`,
    MODIFY COLUMN `current_size` bigint NULL DEFAULT 0 COMMENT '当前空间下图片的总大小' AFTER `space_max_number`,
    MODIFY COLUMN `current_count` bigint NULL DEFAULT 0 COMMENT '当前空间下图片的总数量' AFTER `current_size`;