create table if not exists products
(
    id          uuid         not null primary key default gen_random_uuid(),
    title       varchar(255) not null check ( length(title) > 0 ),
    price       float        not null check ( price > 0 ),
    description text         not null check ( length(description) > 0 ),
    created_at  timestamp    not null             default current_timestamp,
    updated_at  timestamp    not null             default current_timestamp,
    category_id uuid         not null references categories (id)
);

insert into products(id, title, price, description, category_id)
values ('5defee46-97a1-49ee-9d3a-a35ee33b74e9', 'iPhone 15', 500000.0,
        'New iPhone 15', 'f64d05d7-158e-46b7-abf7-5c2724760228'), -- category Phone
       ('c72eabaf-30c3-4e6c-ab1b-3692d037eca2', 'Bread', 200.0,
        'White bread', '9bda8268-644a-45cc-9440-1782f9d258f0'); -- category Food