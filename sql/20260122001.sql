CREATE TABLE `yuxi`.`picture_space`  (
                                    `id` bigint NOT NULL COMMENT '主键ID',
                                    `space_name` varchar(128) NOT NULL COMMENT '空间名称',
                                    `space_level` tinyint NOT NULL COMMENT '空间级别： 0 普通版 1专业版 2旗舰版',
                                    `space_max_size` decimal(10, 2) NULL COMMENT '空间的大小',
                                    `space_max_number` bigint NULL COMMENT '空间的图片容量',
                                    `current_size` decimal(10, 2) NULL COMMENT '当前空间下图片的总大小',
                                    `current_count` bigint NULL COMMENT '当前空间下图片的总数量',
                                    `create_user` bigint NOT NULL COMMENT '创建用户id',
                                    `update_user` bigint NOT NULL COMMENT '修改用户id',
                                    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                    `edit_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
                                    PRIMARY KEY (`id`)
) COMMENT = '图片空间表';