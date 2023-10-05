CREATE SEQUENCE table_id_seq;

create table client
(
    id integer NOT NULL DEFAULT nextval('table_id_seq'),
    code int,
    name varchar(250),
    sex varchar(10),
    birth_date date
);
create table product
(
    id integer NOT NULL DEFAULT nextval('table_id_seq'),
    code int,
    name varchar(250)
);
create table sales
(
    id integer NOT NULL DEFAULT nextval('table_id_seq'),
    code int,
    client_code int,
    product_code int,
    amount_rub int
);

create table stg_client
(
    code int,
    name varchar(250),
    sex varchar(10),
    birth_date date
);
create table stg_product
(
    code int,
    name varchar(250)
);
create table stg_sales
(
    code int,
    client_code int,
    product_code int,
    amount_rub int
);

