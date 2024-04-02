create table if not exists products
(
    id          uuid         not null primary key default gen_random_uuid(),
    title       varchar(255) not null check ( length(title) > 0 ),
    price       float        not null check ( price > 0 ),
    description text         not null check ( length(description) > 0 ),
    category_id uuid         not null references categories (id)
);