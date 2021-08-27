-- since 0.0.1
create table `fund`(
`uuid` varchar(64) NOT NULL,
`id` varchar(32) NOT NULL COMMENT '基金id',
`name` varchar(32) COMMENT '基金名称',
`manager_id` varchar(64) COMMENT '基金经理id',
`manager_name` varchar(64) COMMENT '基金经理',
`send_status` varchar(6) NOT NULL DEFAULT '0' COMMENT '发送状态：0-初始状态，1-待发送，2-发送中，3-发送超时，4-发送成功，9-发送失败',
`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
) engine innodb;