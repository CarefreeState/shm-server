
drop database if exists `home_school_management`;
create database `home_school_management` character set utf8mb4 collate utf8mb4_bin;
use `home_school_management`;

drop table if exists `user`;
create table `user`(
    `id` bigint primary key auto_increment comment '用户唯一 id',
    `username` varchar(32) unique not null default '' comment '用户名',
    `password` varchar(128) not null default '' comment '密码',
    `nickname` varchar(32) not null default '' comment '用户昵称',
    `phone_number` char(11) not null default '' comment '手机号码',
    `user_type` int not null comment '用户类型：1. 管理员 2. 老师 3. 家长',
    `audit_status` int not null default 0 comment '审核状态：0. 未审核, 1. 审核通过, 2. 审核不通过',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    unique index `uni_username`(`username` asc) using btree
) auto_increment = 10000 comment = '用户基本信息表';

insert into user(`username`, `nickname`, `password`, `user_type`, `audit_status`)
    values('root', 'root', '8f687f9a47e14aaf92f5d861355d3cce$8960081935ccd38123476bb232573024', 1, 1);
insert into user(`username`, `nickname`, `password`, `user_type`, `audit_status`)
    values('mms', '马铭胜', '8f687f9a47e14aaf92f5d861355d3cce$8960081935ccd38123476bb232573024', 1, 1);


drop table if exists `school_class`;
create table `school_class` (
    `id` bigint primary key auto_increment comment 'id',
    `creator_id` bigint not null comment '创建者 id',
    `class_name` varchar(32) not null default '' comment '班级名',
    `audit_status` int not null default 0 comment '审核状态：0. 未审核, 1. 审核通过, 2. 审核不通过',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_creator_id`(`creator_id` asc) using btree
) comment '班级表';

drop table if exists `class_user_link`;
create table `class_user_link` (
    `id` bigint primary key auto_increment comment 'id',
    `class_id` bigint not null comment '班级 id',
    `user_id` bigint not null comment '老师 id',
    `audit_status` int not null default 0 comment '审核状态：0. 未审核, 1. 审核通过, 2. 审核不通过',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_cu_id`(`class_id` asc, `user_id` asc) using btree
) comment '班级-用户关联表';


drop table if exists `system_message`;
create table `system_message` (
    `id` bigint primary key auto_increment comment 'id',
    `creator_id` bigint not null comment '创建者 id',
    `title` varchar(256) not null comment '标题',
    `content` text not null comment '内容',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_creator_id`(`creator_id` asc) using btree
) comment '系统消息表';


drop table if exists `class_message`;
create table `class_message` (
    `id` bigint primary key auto_increment comment 'id',
    `creator_id` bigint not null comment '创建者 id',
    `class_id` bigint not null comment '班级 id',
    `title` varchar(256) not null comment '标题',
    `content` text not null comment '内容',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_creator_id`(`creator_id` asc) using btree,
    index `idx_class_id`(`class_id` asc) using btree
) comment '班级通知表';

drop table if exists `site_message`;
create table `site_message` (
    `id` bigint primary key auto_increment comment 'id',
    `class_id` bigint not null comment '班级 id',
    `sender_id` bigint not null comment '发送者 id',
    `recipient_id` bigint not null comment '接受者 id',
    `title` varchar(256) not null comment '标题',
    `content` text not null comment '内容',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_class_id`(`class_id` asc) using btree,
    index `idx_sender_id`(`sender_id` asc) using btree,
    index `idx_recipient_id`(`recipient_id` asc) using btree
) comment '站内信表';

