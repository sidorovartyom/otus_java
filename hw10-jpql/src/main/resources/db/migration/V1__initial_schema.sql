-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
/*
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

 */

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence seq start with 1 increment by 1;

create table client
(
    id   bigint not null primary key,
    name varchar(50)
);
create table phone
(
    id   bigint not null primary key,
    number varchar(50),
    client_id bigint REFERENCES client(id)
);

create table address
(
    id   bigint not null primary key,
    street varchar(50)
);

alter table client add column address_id bigint
    CONSTRAINT fk_address_id REFERENCES address(id);