CREATE TABLE `yuxi`.`picture_and_picture_space`  (
                                    `id` bigint NOT NULL COMMENT '主键ID',
                                    `picture_id` bigint NOT NULL COMMENT '图片ID',
                                    `picture_space_id` bigint NOT NULL COMMENT '空间ID',
                                    `create_user` bigint NOT NULL COMMENT '创建用户id',
                                    `update_user` bigint NOT NULL COMMENT '修改用户id',
                                    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                    `edit_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
                                    PRIMARY KEY (`id`)
) COMMENT = '图片和图片空间的关联表';