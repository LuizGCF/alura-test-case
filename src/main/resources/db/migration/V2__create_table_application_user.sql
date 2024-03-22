CREATE TABLE application_user(
    id int auto_increment primary key,
    name varchar(255) not null,
    username varchar(20) not null unique,
    email varchar(255) not null unique,
    password varchar(255) not null,
    role varchar(9) not null,
    creation_date DATE
);