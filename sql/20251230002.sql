alter table picture
    modify review_user bigint null comment '审核人';

alter table picture
    modify review_time datetime null comment '审核时间';

