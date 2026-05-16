-- 题库表
drop table if exists question_bank;
create table if not exists question_bank
(
    id          bigint auto_increment comment 'id' primary key,
    title       varchar(256)                       null comment '标题',
    description text                               null comment '描述',
    picture     varchar(2048)                      null comment '图片',
    user_id      bigint                             not null comment '创建用户 id',
    edit_time    datetime default CURRENT_TIMESTAMP not null comment '编辑时间',
    create_time  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete    tinyint  default 0                 not null comment '是否删除',
    index idx_title (title)
) comment '题库' collate = utf8mb4_unicode_ci;

-- 题目表
drop table if exists question;
create table if not exists question
(
    id         bigint auto_increment comment 'id' primary key,
    title      varchar(256)                       null comment '标题',
    content    text                               null comment '内容',
    tags       varchar(1024)                      null comment '标签列表（json 数组）',
    answer     text                               null comment '推荐答案',
    user_id     bigint                             not null comment '创建用户 id',
    edit_time   datetime default CURRENT_TIMESTAMP not null comment '编辑时间',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete   tinyint  default 0                 not null comment '是否删除',
    index idx_title (title),
    index idx_userId (user_id)
) comment '题目' collate = utf8mb4_unicode_ci;

-- 题库题目表（硬删除）
drop table if exists question_bank_question;
create table if not exists question_bank_question
(
    id             bigint auto_increment comment 'id' primary key,
    question_bank_id bigint                             not null comment '题库 id',
    question_id     bigint                             not null comment '题目 id',
    user_id         bigint                             not null comment '创建用户 id',
    create_time     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    UNIQUE (question_bank_id, question_id)
) comment '题库题目' collate = utf8mb4_unicode_ci;
