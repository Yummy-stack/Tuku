ALTER TABLE `yuxi`.`picture`
    MODIFY COLUMN `pic_url` varchar(200) NOT NULL DEFAULT '' COMMENT '图片url路径' AFTER `pic_name`;