-- noinspection SqlNoDataSourceInspectionForFile

create schema `unique_seq` collate utf8mb4_bin;

-- auto-generated definition
create table `unique_seq`.`seq_block`
(
    seq_name    varchar(256)                              not null comment '序列名',
    seq_max     bigint unsigned                           not null comment '最大值',
    seq_step    bigint unsigned default 100               not null comment '步阶',
    seq_desc    varchar(256)                              null comment '序列描述说明',
    seq_group   varchar(256)                              null comment '序列组',
    add_time    datetime        default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime        default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    primary key(seq_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `unique_seq`.seq_block (seq_name, seq_max, seq_step, seq_desc, seq_group, add_time, update_time) VALUES ('commons', 10000, 200, '默认序列块', 'default', now(), now());