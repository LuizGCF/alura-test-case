CREATE TABLE course(
    id int auto_increment primary key,
    id_instructor int not null,
    name varchar(255) not null,
    code varchar(20) not null unique,
    description varchar(255) not null,
    status varchar(8) not null,
    creation_date DATE not null,
    inactivation_date DATE,

    CONSTRAINT FK_InstructorCourse FOREIGN KEY (id_instructor)
    REFERENCES application_user(id)
);