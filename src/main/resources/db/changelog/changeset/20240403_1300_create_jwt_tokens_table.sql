create table if not exists jwt_tokens
(
    id         uuid primary key not null default gen_random_uuid(),
    token      text             not null unique,
    type       varchar(20)      not null check ( type in ('ACCESS', 'REFRESH') ),
    created_at timestamp        not null default current_timestamp,
    user_id    uuid             not null references users (id)
);

create unique index unq_jwt_tokens_token on jwt_tokens(token);