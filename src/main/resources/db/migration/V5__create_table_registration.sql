CREATE TABLE registration(
    id int auto_increment primary key,
    id_user int not null,
    id_course int not null,
    registration_date DATE not null,

    CONSTRAINT FK_UserRegistration FOREIGN KEY(id_user) REFERENCES application_user(id),
    CONSTRAINT FK_CourseRegistration FOREIGN KEY(id_course) REFERENCES course(id),
    UNIQUE UserCourseUK (id_user,id_course)
);