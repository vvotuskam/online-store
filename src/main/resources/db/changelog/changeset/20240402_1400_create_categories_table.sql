create table if not exists categories
(
    id   uuid         not null primary key default gen_random_uuid(),
    name varchar(255) not null check ( length(name) > 0 )
);

insert into categories(id, name)
values ('f64d05d7-158e-46b7-abf7-5c2724760228', 'Phone'),
       ('9bda8268-644a-45cc-9440-1782f9d258f0', 'Food'),
       ('dc6bc9db-aa83-4cba-9b6e-e6c82d021c5d', 'Game');