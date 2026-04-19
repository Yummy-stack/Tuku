alter table picture
    add review_status tinyint default 0 null comment '审核状态 0待审核 1通过 2不通过';

alter table picture
    add review_message varchar(512) default '' null comment '审核信息';

alter table picture
    add review_user bigint not null comment '审核人';

alter table picture
    add review_time datetime not null  comment '审核时间';

