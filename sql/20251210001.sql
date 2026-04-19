-- 创建图片表
CREATE TABLE `picture` (
                           `id` bigint NOT NULL COMMENT '主键ID',
                           `pic_name` varchar(100) NOT NULL DEFAULT '' COMMENT '图片名称',
                           `pic_url` varchar(200) NOT NULL DEFAULT '' COMMENT '图片存储路径',
                           `pic_category` varchar(255) NOT NULL DEFAULT '' COMMENT '图片种类',
                           `pic_tags` varchar(512) DEFAULT '' COMMENT '图片标签',
                           `pic_width` int NOT NULL COMMENT '图片宽度',
                           `pic_height` int NOT NULL COMMENT '图片高度',
                           `pic_size` bigint NOT NULL COMMENT '图片体积',
                           `pic_scale` decimal(10,2) NOT NULL COMMENT '图片宽高比例',
                           `create_user` bigint NOT NULL COMMENT '创建人',
                           `create_time` datetime NOT NULL COMMENT '创建时间',
                           `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                           `edit_time` datetime NOT NULL COMMENT '编辑时间',
                           `is_delete` tinyint NOT NULL COMMENT '是否删除 0存在 1删除',
                           `pic_format` varchar(32) DEFAULT NULL COMMENT '图片格式',
                           `pic_introduction` longtext COMMENT '图片简介',
                           `update_user` bigint NOT NULL COMMENT '修改人',
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='图片表';