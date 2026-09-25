create table  users
(
    id       UUID PRIMARY KEY,
    username varchar(50)   not null unique,
    email    varchar(255)   not null unique,
    password varchar(1024) not null
);

create index idx_users_email on users(email);
create index idx_users_username on users(username);
