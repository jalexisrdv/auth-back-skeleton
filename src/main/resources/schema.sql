drop table if exists users cascade;
drop table if exists authorities cascade;

create table users (
    username varchar(50) not null primary key,
    password varchar(500) null,
    enabled boolean not null,
    verification_code varchar(100) null,
    verification_code_expiration_date TIMESTAMP WITHOUT TIME ZONE null
);

create table authorities (
    username varchar(50) not null,
    authority varchar(50) not null,
    constraint fk_authorities_users foreign key (username) references users(username)
);

create unique index idx_auth_username on authorities (username, authority);