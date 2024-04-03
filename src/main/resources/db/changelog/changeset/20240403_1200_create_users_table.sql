create table if not exists users
(
    id       uuid primary key not null default gen_random_uuid(),
    username varchar          not null unique,
    password text             not null
);

create unique index unq_user_username on users(username);

-- insert into users(username, password)
-- values ('user', )