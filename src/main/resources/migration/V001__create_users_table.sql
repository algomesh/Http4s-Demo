create table users
(
    id           uuid primary key,
    email        varchar(50) not null unique,
    first_name   varchar(20) not null,
    last_name    varchar(20),
    dob          date,
    created_date date,
    is_enabled   boolean
)
