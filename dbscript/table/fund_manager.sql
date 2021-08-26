-- since 0.0.1
create table `fund_manager`(
`uuid` varchar(64) NOT NULL,
`name` varchar(32) COMMENT '基金经理',
`fund_id` varchar(64) COMMENT '基金编码',
`employment_date` varchar(64) COMMENT '基金经理任职时间',
`send_flag` varchar(6) NOT NULL DEFAULT '0' COMMENT '发送状态：0-未发送，1-已发送',
`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
) engine innodb;