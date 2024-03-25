CREATE TABLE feedback(
    id int auto_increment primary key,
    title varchar(50),
    description varchar(500),
    rating int not null,
    feedback_date DATE not null
);

ALTER TABLE registration ADD id_feedback int;

ALTER TABLE registration ADD CONSTRAINT FK_FeedbackRegistration FOREIGN KEY (id_feedback) REFERENCES feedback (id);