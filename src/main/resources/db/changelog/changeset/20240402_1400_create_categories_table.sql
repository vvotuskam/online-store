create table if not exists categories
(
    id   uuid         not null primary key default gen_random_uuid(),
    name varchar(255) not null check ( length(name) > 0 )
);