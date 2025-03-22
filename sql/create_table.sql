# 数据库初始化

-- 创建库
create database if not exists forceKindle;

-- 切换库
use forceKindle;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    unionId      varchar(256)                           null comment '微信开放平台id',
    mpOpenId     varchar(256)                           null comment '公众号openId',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_unionId (unionId)
) comment '用户' collate = utf8mb4_unicode_ci;


-- 子弹表
create table if not exists bullet
(
    id           bigint auto_increment comment 'id' primary key,
    bulletName     varchar(256)                           null comment '子弹名称',
    bulletPrice   varchar(1024)                          null comment '子弹价格',
    bulletImage   varchar(1024)                          null comment '子弹图片',
    bulletDescription  varchar(512)                           null comment '子弹描述',
    bulletType     varchar(256)                          null comment '子弹类型',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_unionId (bulletName)
) comment '子弹' collate = utf8mb4_unicode_ci;
