ALTER TABLE `yuxi`.`picture`
    MODIFY COLUMN `review_user` bigint NULL COMMENT '审核人' AFTER `review_message`;