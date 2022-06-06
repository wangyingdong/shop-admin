create table if not exists attribute
(
    id          int auto_increment
    constraint `PRIMARY`
    primary key,
    name        varchar(50) charset utf8 null,
    category_Id int                      null,
    type        varchar(20)              null
    );

create table if not exists attribute_value
(
    id           int auto_increment
    constraint `PRIMARY`
    primary key,
    attribute_id int                      not null,
    value        varchar(50) charset utf8 not null
    );

create table if not exists category
(
    id        int auto_increment
    constraint `PRIMARY`
    primary key,
    name      varchar(20) charset utf8 null,
    path      varchar(20)              null,
    parent_id int                      null,
    sort      int                      null,
    level     int                      null,
    state     tinyint(1) default 1     null
    );

create table if not exists good
(
    id               int auto_increment
    constraint `PRIMARY`
    primary key,
    name             varchar(50) charset utf8   not null,
    category_id      int                        not null,
    datetime         datetime                   not null,
    price            decimal(10, 2)             null,
    images           varchar(500)               null,
    amount           int default 0              not null,
    attribute_values varchar(100) charset utf8  null,
    content          varchar(8000) charset utf8 null
    );

create table if not exists good_attribute_value
(
    good_id            int not null,
    attribute_value_id int not null,
    constraint `PRIMARY`
    primary key (good_id, attribute_value_id)
    );

create table if not exists module
(
    id        int auto_increment,
    name      varchar(20) charset utf8 null,
    path      varchar(50) charset utf8 null,
    parent_id int                      null,
    sort      int                      null,
    level     int                      null,
    constraint module_id_uindex
    unique (id)
    );

alter table module
    add constraint `PRIMARY`
        primary key (id);

create table if not exists orders
(
    id       varchar(50)                                    not null,
    name     varchar(50) collate utf8_unicode_ci default '' not null,
    amount   int                                            not null,
    price    decimal(10, 2)                                 not null,
    datetime datetime                                       not null,
    state    int                                            null,
    address  varchar(50) collate utf8_unicode_ci default '' not null,
    good_id  int                                            not null,
    constraint orders_id_uindex
    unique (id)
    );

alter table orders
    add constraint `PRIMARY`
        primary key (id);

create table if not exists orders_logistic
(
    id       int auto_increment
    constraint `PRIMARY`
    primary key,
    order_id varchar(50)                                     null,
    content  varchar(100) collate utf8_unicode_ci default '' not null,
    datetime datetime                                        null
    );

create table if not exists role
(
    id          int auto_increment,
    role_name   varchar(50) charset utf8 null,
    role_remark varchar(50) charset utf8 null,
    state       tinyint(1)               null,
    constraint roles_id_uindex
    unique (id)
    );

alter table role
    add constraint `PRIMARY`
        primary key (id);

create table if not exists role_module
(
    role_id   int not null,
    module_id int not null,
    constraint `PRIMARY`
    primary key (role_id, module_id)
    );

create table if not exists user_info
(
    id       bigint auto_increment,
    username varchar(20)              null,
    password varchar(20)              null,
    address  varchar(50) charset utf8 null,
    email    varchar(50)              null,
    mobile   varchar(11)              null,
    state    tinyint(1) default 1     null,
    constraint user_info_id_uindex
    unique (id),
    constraint user_info_username_uindex
    unique (username)
    );

alter table user_info
    add constraint `PRIMARY`
        primary key (id);

create table if not exists user_role
(
    user_id bigint null,
    role_id int    null,
    constraint user_role_pk
    unique (user_id, role_id)
    );

